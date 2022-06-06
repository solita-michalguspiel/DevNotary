//
//  LocalNoteView.swift
//  iosApp
//
//  Created by Michal Guspiel on 1.6.2022.
//

import SwiftUI
import shared


class LocaLNoteViewHelper : ObservableObject{
    
    
    var viewModel = iosDI().getNotesDetailViewModel()

    @Published var displayedNote = Note.init(noteId: "", ownerUserId: nil, title: "", content: "", dateTime: "", color: "")
        
    @Published var shouldPopBackStack : Bool = false
    
    @Published var sharedOwnerUser : User = User.init(userId: "", userEmail: "")
    
    
    var listeners : [Closeable] = []
        
    func start(){
        
      let displayedNoteListener = self.viewModel.watch(viewModel.displayedNote, block: { note in
            self.displayedNote = note as! Note
            print("LOCAL Received new note! : \(note.debugDescription)")
        })
       let noteModificationStatusListner = self.viewModel.watch(viewModel.noteModificationStatus, block: { response in
            if(response is ResponseSuccess<AnyObject>){
                let opResponse = response as! ResponseSuccess<shared.Operation>
                switch (opResponse.data){
                case _ as shared.Operation.Delete:
                    self.shouldPopBackStack = true
                case _ as shared.Operation.Add:
                    self.shouldPopBackStack = true
                default :
                    print("Default")
                }
            }
        })
        
        let noteSharingStateListener = self.viewModel.watch(viewModel.noteSharingState,block : { response in
            if(response is ResponseSuccess<AnyObject>){
                self.shouldPopBackStack = true
            }
        })
        
        let noteOwnerUserListener = self.viewModel.watch(viewModel.noteOwnerUser,block : {response in
            print("Note owner user listener response! :")
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

struct LocalNoteView: View {
        
    @ObservedObject var stateObject : LocaLNoteViewHelper = LocaLNoteViewHelper()
    @EnvironmentObject var appState: AppState
        
    @State var navigateToAddNewNote = false
    @State var navigateToEditNote = false
    @State var isShareSheetOpen = false
        
    let note: Note
    
    init(note: Note){
        self.note = note
    }
    
    var body: some View {
        VStack{
            NavigationLink(destination: AddAndEditNoteView.init(editedNote: nil),isActive: $navigateToAddNewNote){
                EmptyView()
            }
            NavigationLink(destination: AddAndEditNoteView.init(editedNote: note),isActive: $navigateToEditNote){
                EmptyView()
            }
            
            ZStack{
                RoundedRectangle(cornerRadius: 20,style: .continuous)
                    .fill(NoteColor.init(color: stateObject.displayedNote.color).getColor())
                
                GeometryReader{ geo in
                    VStack{
                        
                        if(stateObject.sharedOwnerUser.userEmail != "" && !isLocal()){
                            Text("Shared by \(stateObject.sharedOwnerUser.userEmail)")
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
                                Button(action:{}){
                                    Image(systemName: "person.2.fill")
                                        .resizable()
                                        .scaledToFit()
                                        .tint(.black)
                                        .frame(width: 32, height: 32)
                                }
                            }.padding(.vertical,3)
                                .padding(.horizontal,10)
                        }
                        TextField("Note title",text: .constant(stateObject.displayedNote.title))
                            .disabled(true)
                            .font(.title)
                            .padding(.horizontal)
                            .padding(.vertical,10)
                        Divider()
                        TextEditor(text : .constant(stateObject.displayedNote.content))
                            .disabled(true)
                            .background(.clear)
                            .frame(height: geo.size.height * 0.7)
                            .textFieldStyle(PlainTextFieldStyle())
                            .padding(.horizontal,10)
                    }
                }
            }.padding()
            HStack{
                Spacer()
                if(isLocal()){
                    CustomBorderedButton(text : "New note"){
                        navigateToAddNewNote = true
                    }.padding(.horizontal,3)
                    CustomBorderedButton(text : "Delete note"){
                        stateObject.viewModel.deleteNote()
                    }.padding(.horizontal,3)
                    CustomBorderedButton(text  : "Edit note"){
                        navigateToEditNote = true
                    }.padding(.horizontal,3)
                }
                else{
                    CustomBorderedButton(text : "Delete note"){
                        stateObject.viewModel.deleteOwnAccessFromSharedNote()
                    }.padding(.horizontal,3)
                    CustomBorderedButton(text : "Save note locally"){
                        stateObject.viewModel.addNote(providedId: nil)
                    }.padding(.horizontal,3)
                }
            }.padding()
        }
        .sheet(isPresented: $isShareSheetOpen){
            ShareNoteSheet()
        }
        .onAppear{
            print("Local note view Appeared!")
            stateObject.start()
            stateObject.viewModel.prepareNoteScreen(note: note)
            print("Checking note: \(note.debugDescription)")
        }.onDisappear{
            stateObject.stop()
        }
        .background(Color.background)
        .navigationBarBackButtonHidden(true)
        .toolbar(){
            ToolbarItem(placement: .navigationBarLeading){
                Button(action:{
                    self.appState.moveToDashboard = true
                    self.appState.selectedTab = 2
                }
                ){
                Image(systemName: "arrow.left")
            }
        }
        }
        if stateObject.shouldPopBackStack {
            Text("").onAppear(){
                if(!isShareSheetOpen){
                    self.appState.moveToDashboard = true
                    self.appState.selectedTab = 2
                }
                stateObject.viewModel.resetNoteModificationStatus()
                stateObject.shouldPopBackStack = false
                }
            }
    }
    
    func isLocal() -> Bool{
        return note.ownerUserId == nil
    }
}
