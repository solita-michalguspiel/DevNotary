//
//  AddNewNoteView.swift
//  iosApp
//
//  Created by Michal Guspiel on 30.5.2022.
//

import Foundation
import SwiftUI
import shared
import Combine

class NoteData : ObservableObject{
    
    let viewModel = iosDI().getNotesDetailViewModel()
    
    @Published var addedNote : Note? = nil
    @Published var displayedNote = Note.init(noteId: "", ownerUserId: nil, title: "", content: "", dateTime: "", color: "")
    @Published var navSelection : String? = nil
    @Published var title = ""{
        didSet{
            viewModel.changeTitleInput(newTitle: self.title)
        }
    }
    @Published var content = ""{
        didSet{
            viewModel.changeContentInput(newContent: self.content)
        }
    }
    
    var listeners : [Closeable] = []
    
    func start(){
        viewModel.watch(viewModel.displayedNote, block: { note in
            self.displayedNote = note as! Note
            self.content = self.displayedNote.content
            self.title = self.displayedNote.title
        }).addToListenerList(list: &listeners)
        
        viewModel.watch(viewModel.noteModificationStatus,block : { response in
            if(response is ResponseSuccess<AnyObject>){
                let opResponse = response as! ResponseSuccess<shared.Operation>
                switch (opResponse.data){
                case _ as shared.Operation.Add:
                    self.addedNote = opResponse.data?.note
                    self.navSelection = Constants.NOTE_DETAILS_VIEW
                case _ as shared.Operation.Edit:
                    self.navSelection = Constants.POP_NAVIGATION
                default :
                    print("Default")
                }
            }
        }).addToListenerList(list: &listeners)
    }
    
    func stop(){
        listeners.forEach{ listener in
            listener.close()
        }
    }
}

struct NoteInteractionView : View{
    let editedNote: Note?
    @EnvironmentObject var appState: AppState
    @StateObject var noteData = NoteData()
    
    init(editedNote: Note?){
        self.editedNote = editedNote
        UITextView.appearance().backgroundColor = .clear
    }
    
    var body : some View{
        
        let navigationTitle = editedNote == nil ? "Add note" : "Edit note"
        VStack{
            if(noteData.addedNote != nil){
                NavigationLink(destination : NoteDetailsView(note: noteData.addedNote!),tag : Constants.NOTE_DETAILS_VIEW, selection: $noteData.navSelection){
                    Text("").onAppear{
                        noteData.viewModel.resetNoteModificationStatus()
                    }
                }
            }
            ZStack{
                RoundedRectangle(cornerRadius: 20,style: .continuous)
                    .fill(NoteColor.init(color: noteData.displayedNote.color).getColor())
                GeometryReader{ geo in
                    VStack{
                        TextField("Note title",text: $noteData.title)
                            .font(.title)
                            .padding(.horizontal)
                            .padding(.vertical,10)
                        Divider()
                        TextEditor(text : $noteData.content)
                            .background(.clear)
                            .frame(height: geo.size.height * 0.7)
                            .textFieldStyle(PlainTextFieldStyle())
                            .padding(.horizontal,10)
                        Divider()
                        BallsRow(
                            chosenColor: noteData.displayedNote.color,
                            pickColor : { color in noteData.viewModel.changeNoteColor(newColor: color)}
                        ).frame(height: geo.size.height * 0.15,alignment: .center)
                    }
                }
            }.padding()
            HStack{
                Spacer()
                if(editedNote == nil){
                    CustomBorderedButton(text  : "Add note"){
                        noteData.viewModel.addNote(providedId: nil)
                    }.padding()
                }
                else{
                    CustomBorderedButton(text  : "Save note"){
                        noteData.viewModel.editNote()
                    }.padding()
                }
            }
        }.onAppear{
            noteData.viewModel.prepareNoteScreen(note: editedNote)
            noteData.start()
        }.onReceive(noteData.$navSelection, perform: { selection in
            if(selection == Constants.POP_NAVIGATION){
                self.appState.popToRootAndShowNotesList()
                noteData.viewModel.resetNoteModificationStatus()
            }
        })
        .background(Color.background)
        .onDisappear{noteData.stop()}
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
    }
}
