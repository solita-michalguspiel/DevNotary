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

class AddNewNoteViewHelper : ObservableObject{
    
    var viewModel = iosDI().getNotesDetailViewModel()
    
    @Published var addedNote : Note? = nil
    
    @Published var displayedNote = Note.init(noteId: "", ownerUserId: nil, title: "", content: "", dateTime: "", color: "")

    @Published var shouldNavigate = false
    
    @Published var shouldPopNavigation = false
    
    var listeners : [Closeable] = []
    
    let limit = 30
    @Published var title = ""{
        didSet{
            if title.count > limit {
                self.title = String(title.prefix(limit))
                viewModel.changeTitleInput(newTitle: String(title.prefix(limit)))
            }
            else {
                viewModel.changeTitleInput(newTitle: String(title.prefix(limit)))
            }
        }
    }
    
    @Published var content = ""{
        didSet{
            viewModel.changeContentInput(newContent: self.content)
        }
    }
    
    func start(){
        
        let displayedNoteListener = self.viewModel.watch(viewModel.displayedNote, block: { note in
            self.displayedNote = note as! Note
            self.title = self.displayedNote.title
            self.content = self.displayedNote.content
            print("ADD NEW Received new note! : \(note.debugDescription)")
        })
        
        let noteModStatusListener = self.viewModel.watch(viewModel.noteModificationStatus,block : { response in
            if(response is ResponseSuccess<AnyObject>){
                let opResponse = response as! ResponseSuccess<shared.Operation>
                switch (opResponse.data){
                case _ as shared.Operation.Add:
                    self.addedNote = opResponse.data?.note
                    self.shouldNavigate = true
                case _ as shared.Operation.Edit:
                    self.shouldPopNavigation = true
                    print("Edited!!! should pop navigation changed to true!")
                default :
                    print("Default")
                }
            }
        })
        
        listeners.append(displayedNoteListener)
        listeners.append(noteModStatusListener)
    }
    
    func stop(){
        listeners.forEach{ listener in
            listener.close()
        }
    }
    
    
}


struct AddAndEditNoteView : View{
    
    let editedNote: Note?
    
    @EnvironmentObject var appState: AppState
    @State var content = ""
    @StateObject var stateObject = AddNewNoteViewHelper()
    
    init(editedNote: Note?){
            self.editedNote = editedNote
            UITextView.appearance().backgroundColor = .clear
        }
    
    
    var body : some View{
        
        VStack{
            if(stateObject.addedNote != nil){
                    NavigationLink(destination : LocalNoteView(note: stateObject.addedNote!)
                                   ,isActive: $stateObject.shouldNavigate){
                        Text("").onAppear{
                            print("On appear, reset note modification status!!!")
                            stateObject.viewModel.resetNoteModificationStatus()
                        }
                    }
                }
            
            ZStack{
                RoundedRectangle(cornerRadius: 20,style: .continuous)
                    .fill(NoteColor.init(color: stateObject.displayedNote.color).getColor())
                
                GeometryReader{ geo in
                    VStack{
                        TextField("Note title",text: $stateObject.title)
                            .font(.title)
                            .padding(.horizontal)
                            .padding(.vertical,10)
                        Divider()
                        TextEditor(text : $stateObject.content)
                            .background(.clear)
                            .frame(height: geo.size.height * 0.7)
                            .textFieldStyle(PlainTextFieldStyle())
                            .padding(.horizontal,10)
                           
                        Divider()
                        BallsRow(
                            chosenColor: stateObject.displayedNote.color,
                                    pickColor : { color in stateObject.viewModel.changeNoteColor(newColor: color)}
                        ).frame(height: geo.size.height * 0.15,alignment: .center)
                        Spacer()
                       
                    }
                }
            }.padding()
            HStack{
                Spacer()
                if(editedNote == nil){
                    CustomBorderedButton(text  : "Add note"){
                        stateObject.viewModel.addNote(providedId: nil)
                    }.padding()
                }
                else{
                    CustomBorderedButton(text  : "Save note"){
                        stateObject.viewModel.editNote()
                    }.padding()
                }
            }
        }.onAppear{
            stateObject.viewModel.prepareNoteScreen(note: editedNote)
            stateObject.start()
        }.onReceive(stateObject.$shouldPopNavigation, perform: { bool in
            if(bool){
                self.appState.moveToDashboard = true
                self.appState.selectedTab = 2
                stateObject.viewModel.resetNoteModificationStatus()
            }
        })
        .onDisappear{stateObject.stop()}
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
    }
    
}
