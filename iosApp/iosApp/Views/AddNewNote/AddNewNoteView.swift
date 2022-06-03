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
    
    var notesViewModel = iosDI().getNotesViewModel()
    
    @Published var addedNote : Note? = nil
    
    @Published var displayedNote = Note.init(noteId: "", ownerUserId: nil, title: "", content: "", dateTime: "", color: "")

    
    @Published var shouldNavigate = false
    
    
    let limit = 30
    @Published var title = ""{
        didSet{
            if title.count > limit {
                self.title = String(title.prefix(limit))
                notesViewModel.changeTitleInput(newTitle: String(title.prefix(limit)))
            }
            else {
                notesViewModel.changeTitleInput(newTitle: String(title.prefix(limit)))
            }
        }
    }
    
    init(){
        notesViewModel.prepareNoteScreen(note: nil)
        start()
    }
    
    func start(){
        
        self.notesViewModel.watch(notesViewModel.displayedNote, block: { note in
            self.displayedNote = note as! Note
        })
        
        self.notesViewModel.watch(notesViewModel.noteModificationStatus,block : { response in
            if(response is ResponseSuccess<AnyObject>){
                let opResponse = response as! ResponseSuccess<shared.Operation>
                switch (opResponse.data){
                case _ as shared.Operation.Add:
                    self.addedNote = opResponse.data?.note
                    self.shouldNavigate = true
                default :
                    print("Default")
                }
            }
        })
        
    }
    
    
}


struct AddNewNoteView : View{
    

    @State var content = ""
    @StateObject var stateObject = AddNewNoteViewHelper()
    
    init(){
        UITextView.appearance().backgroundColor = .clear
    }
    
    var body : some View{
    
        let contentBinding = Binding<String>(get: {
            self.content
        }, set: {
            self.content = $0
            stateObject.notesViewModel.changeContentInput(newContent: self.content)
        })
        
        VStack{
            if(stateObject.addedNote != nil){
                
                NavigationLink(destination : LocalNoteView(note: stateObject.addedNote!)
                               ,isActive: $stateObject.shouldNavigate){
                    Text("").onAppear{
                        stateObject.notesViewModel.resetNoteModificationStatus()
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
                        TextEditor(text : contentBinding)
                            .background(.clear)
                            .frame(height: geo.size.height * 0.7)
                            .textFieldStyle(PlainTextFieldStyle())
                            .padding(.horizontal,10)
                           
                        Divider()
                        BallsRow(
                            chosenColor: stateObject.displayedNote.color,
                                    pickColor : { color in stateObject.notesViewModel.changeNoteColor(newColor: color)}
                        ).frame(height: geo.size.height * 0.15,alignment: .center)
                        Spacer()
                       
                    }
                }
            }.padding()
            HStack{
                Spacer()
                CustomBorderedButton(text  : "Add note"){
                    stateObject.notesViewModel.addNote(providedId: nil)
                }.padding()
            }
        }.background(Color.background)
    }
    
}
