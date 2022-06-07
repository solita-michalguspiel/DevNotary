//
//  LocalNoteView.swift
//  iosApp
//
//  Created by Michal Guspiel on 1.6.2022.
//

import SwiftUI
import shared

class NoteDetailsData : ObservableObject{
    
    var viewModel = iosDI().getNotesDetailViewModel()
    
    @Published var displayedNote = Note.init(noteId: "", ownerUserId: nil, title: "", content: "", dateTime: "", color: "")
    @Published var sharedOwnerUser : User = User.init(userId: "", userEmail: "")
    @Published var navSelection : String? = nil
    
    var listeners : [Closeable] = []
    
    func start(){
        
        let displayedNoteListener = self.viewModel.watch(viewModel.displayedNote, block: { note in
            self.displayedNote = note as! Note
        })
        let noteModificationStatusListner = self.viewModel.watch(viewModel.noteModificationStatus, block: { response in
            if(response is ResponseSuccess<AnyObject>){
                let opResponse = response as! ResponseSuccess<shared.Operation>
                switch (opResponse.data){
                case _ as shared.Operation.Delete:
                    self.navSelection = Constants.POP_NAVIGATION
                case _ as shared.Operation.Add:
                    self.navSelection = Constants.POP_NAVIGATION
                default :
                    print("Default")
                }
            }
        })
        
        let noteSharingStateListener = self.viewModel.watch(viewModel.noteSharingState,block : { response in
            if(response is ResponseSuccess<AnyObject>){
                self.navSelection = Constants.POP_NAVIGATION
            }
        })
        
        let noteOwnerUserListener = self.viewModel.watch(viewModel.noteOwnerUser,block : {response in
            print(response.debugDescription)
            if(response is ResponseSuccess<AnyObject>){
                let userResponse = response as! ResponseSuccess<User>
                self.sharedOwnerUser = userResponse.data!
            }
            else {
                self.sharedOwnerUser = User.init(userId: "",userEmail: "")
            }
        })
        
        listeners.append(displayedNoteListener)
        listeners.append(noteModificationStatusListner)
        listeners.append(noteSharingStateListener)
        listeners.append(noteOwnerUserListener)
    }
    
    func stop(){
        listeners.forEach{ listener in
            listener.close()
        }
    }
}

struct NoteDetailsView: View {
    
    @ObservedObject var noteDetailsData : NoteDetailsData = NoteDetailsData()
    @EnvironmentObject var appState: AppState
    @State var isShareSheetOpen = false
    @State var isSharedUserListSheetOpen = false
    
    let note: Note
    
    init(note: Note){
        self.note = note
    }
    
    var body: some View {
        let navigationTitle = isLocal() ? "Local note" : "Shared note"
        VStack{
            NavigationLink(destination: NoteInteractionView.init(editedNote: nil),tag : Constants.ADD_NEW_NOTE_VIEW,selection: $noteDetailsData.navSelection){
                EmptyView()
            }
            NavigationLink(destination: NoteInteractionView.init(editedNote: note),tag: Constants.EDIT_NOTE_VIEW,selection: $noteDetailsData.navSelection){
                EmptyView()
            }
            
            ZStack{
                RoundedRectangle(cornerRadius: 20,style: .continuous)
                    .fill(NoteColor.init(color: noteDetailsData.displayedNote.color).getColor())
                
                GeometryReader{ geo in
                    VStack{
                        if(noteDetailsData.sharedOwnerUser.userEmail != "" && !isLocal()){
                            Text("Shared by \(noteDetailsData.sharedOwnerUser.userEmail)")
                                .font(.caption)
                        }
                        if(isLocal()){
                            HStack{
                                Spacer()
                                Button(action:{
                                    isShareSheetOpen = true
                                }){
                                    Image(systemName: "arrowshape.turn.up.right.fill")
                                        .resizable()
                                        .scaledToFit()
                                        .tint(.black)
                                        .frame(width: 30, height: 30)
                                }.padding(.horizontal,5)
                                Button(action:{
                                    isSharedUserListSheetOpen = true
                                }){
                                    Image(systemName: "person.2.fill")
                                        .resizable()
                                        .scaledToFit()
                                        .tint(.black)
                                        .frame(width: 32, height: 32)
                                }
                            }.padding(.vertical,3)
                                .padding(.horizontal,10)
                        }
                        TextField("Note title",text: .constant(noteDetailsData.displayedNote.title))
                            .disabled(true)
                            .font(.title)
                            .padding(.horizontal)
                            .padding(.vertical,10)
                        Divider()
                        TextEditor(text : .constant(noteDetailsData.displayedNote.content))
                            .disabled(true)
                            .background(.clear)
                            .frame(height: geo.size.height * 0.7)
                            .textFieldStyle(PlainTextFieldStyle())
                            .padding(.horizontal,10)
                    }
                }
            }.padding()
            ButtonsRow(isLocal: isLocal(),noteDetailsData: noteDetailsData)
                .padding()
        }
        .sheet(isPresented: $isShareSheetOpen){
            ShareNoteSheet()
        }
        .sheet(isPresented: $isSharedUserListSheetOpen){
            SharedUsersListSheet()
        }
        .onAppear{
            noteDetailsData.start()
            noteDetailsData.viewModel.prepareNoteScreen(note: note)
        }.onDisappear{
            noteDetailsData.stop()
        }
        .background(Color.background)
        .navigationTitle(navigationTitle)
        .navigationBarBackButtonHidden(true)
        .toolbar(){
            ToolbarItem(placement: .navigationBarLeading){
                CustomBackButton(appState: self.appState)
            }
        }
        if noteDetailsData.navSelection == Constants.POP_NAVIGATION {
            Text("").onAppear(){
                if(!isShareSheetOpen){
                    self.appState.popToRootAndShowNotesList()
                }
                noteDetailsData.viewModel.resetNoteModificationStatus()
                noteDetailsData.navSelection = ""
            }
        }
    }
    
    func isLocal() -> Bool{
        return note.ownerUserId == nil
    }
}

struct ButtonsRow : View{
    let isLocal : Bool
    let noteDetailsData : NoteDetailsData
    var body: some View{
        HStack{
            Spacer()
            if(isLocal){
                CustomBorderedButton(text : "New note"){
                    noteDetailsData.navSelection = Constants.ADD_NEW_NOTE_VIEW
                }.padding(.horizontal,3)
                CustomBorderedButton(text : "Delete note"){
                    noteDetailsData.viewModel.deleteNote()
                }.padding(.horizontal,3)
                CustomBorderedButton(text  : "Edit note"){
                    noteDetailsData.navSelection = Constants.EDIT_NOTE_VIEW
                }.padding(.horizontal,3)
            }
            else{
                CustomBorderedButton(text : "Delete note"){
                    noteDetailsData.viewModel.deleteOwnAccessFromSharedNote()
                }.padding(.horizontal,3)
                CustomBorderedButton(text : "Save note locally"){
                    noteDetailsData.viewModel.addNote(providedId: nil)
                }.padding(.horizontal,3)
            }
        }
    }
}
