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
        viewModel.watch(viewModel.displayedNote, block: { note in
            self.displayedNote = note as! Note
        }).addToListenerList(list: &listeners)
        
        viewModel.watch(viewModel.noteModificationStatus, block: { response in
            if(response is ResponseSuccess<AnyObject>){
                let opResponse = response as! ResponseSuccess<shared.Operation>
                switch (opResponse.data){
                case _ as shared.Operation.Delete:
                    self.navSelection = Constants.POP_NAVIGATION
                case _ as shared.Operation.Add:
                    self.viewModel.prepareNoteScreen(note: opResponse.data?.note)
                    self.viewModel.resetNoteModificationStatus()
                default :
                    print("Default")
                }
            }
        }).addToListenerList(list: &listeners)
        
        viewModel.watch(viewModel.noteSharingState,block : { response in
            if(response is ResponseSuccess<AnyObject>){
                self.navSelection = Constants.POP_NAVIGATION
            }
        }).addToListenerList(list: &listeners)
        
        viewModel.watch(viewModel.noteOwnerUser,block : {response in
            print(response.debugDescription)
            if(response is ResponseSuccess<AnyObject>){
                let userResponse = response as! ResponseSuccess<User>
                self.sharedOwnerUser = userResponse.data!
            }
            else {
                self.sharedOwnerUser = User.init(userId: "",userEmail: "")
            }
        }).addToListenerList(list: &listeners)
    }
    
    func stop(){
        listeners.forEach{ listener in
            listener.close()
        }
    }
}

struct NoteDetailsView: View {
    @StateObject var noteDetailsData : NoteDetailsData = NoteDetailsData()
    @EnvironmentObject var appState: AppState
    @State var isShareSheetOpen = false
    @State var isSharedUserListSheetOpen = false
    
    @State var note: Note
    init(note: Note){
        self.note = note
    }
    
    var body: some View {
        let navigationTitle = note.isLocal() ? "Local note" : "Shared note"
        VStack{
            NavigationLink(destination: NoteInteractionView.init(editedNote: nil),tag : Constants.ADD_NEW_NOTE_VIEW,selection: $noteDetailsData.navSelection){
                EmptyView()
            }
            NavigationLink(destination: NoteInteractionView.init(editedNote: self.noteDetailsData.displayedNote),tag: Constants.EDIT_NOTE_VIEW,selection: $noteDetailsData.navSelection){
                EmptyView()
            }
            
            ZStack{
                RoundedRectangle(cornerRadius: 20,style: .continuous)
                    .fill(NoteColor.init(color: noteDetailsData.displayedNote.color).getColor())
                    VStack{
                        if(noteDetailsData.sharedOwnerUser.userEmail != "" && !note.isLocal()){
                            Text("Shared by \(noteDetailsData.sharedOwnerUser.userEmail)")
                                .font(.caption)
                        }
                        if(note.isLocal()){
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
                            .frame(maxHeight: .infinity)
                            .textFieldStyle(PlainTextFieldStyle())
                            .padding(.horizontal,10)
                        Spacer()
                        HStack{
                            Text("Note created: \(noteDetailsData.viewModel.formatDateTime(date: noteDetailsData.displayedNote.dateTime))")
                                .font(.caption).padding(.leading,20).padding(.bottom,5)
                            Spacer()
                        }
                    }
            }.padding(.horizontal)
            ButtonsRow(isLocal: note.isLocal(),noteDetailsData: noteDetailsData)
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
        }.onChange(of: noteDetailsData.displayedNote){ newNote in
            note = newNote
        }
        
        .background(Color.background)
        .navigationTitle(navigationTitle)
        .navigationBarBackButtonHidden(true)
        .toolbar(){
            ToolbarItem(placement: .navigationBarLeading){
                Button(action:{
                    self.appState.popToRootAndShowNotesList()
                }
                ){
                    Image(systemName: "arrow.left")
                }
            }
        }
        if noteDetailsData.navSelection == Constants.POP_NAVIGATION {
            Text("").onAppear(){
                if(!isShareSheetOpen){
                    self.appState.popToRootAndShowNotesList()
                    noteDetailsData.viewModel.resetNoteModificationStatus()
                    noteDetailsData.viewModel.restartNoteSharingState()
                    noteDetailsData.navSelection = ""
                }
            }
        }
    }
}

struct ButtonsRow : View{
    let isLocal : Bool
    let noteDetailsData : NoteDetailsData
    var body: some View{
        HStack{
            Spacer()
            if(isLocal){
                CustomBorderedButton(text : "New"){
                    noteDetailsData.navSelection = Constants.ADD_NEW_NOTE_VIEW
                }.padding(.horizontal,3)
                CustomBorderedButton(text : "Delete"){
                    noteDetailsData.viewModel.deleteNote()
                }.padding(.horizontal,3)
                CustomBorderedButton(text  : "Edit"){
                    noteDetailsData.navSelection = Constants.EDIT_NOTE_VIEW
                }.padding(.horizontal,3)
            }
            else{
                CustomBorderedButton(text : "Delete"){
                    noteDetailsData.viewModel.deleteOwnAccessFromSharedNote()
                }.padding(.horizontal,3)
                CustomBorderedButton(text : "Save note locally"){
                    noteDetailsData.viewModel.addNote(providedId: nil)
                }.padding(.horizontal,3)
            }
        }
    }
}
