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

    @Published var chosenColor = ""
    
    let limit = 30
    @Published var title = ""{
        didSet{
            if title.count > limit {
                print("Condition satisfied")
                print(title.count)
                self.title = String(title.prefix(limit))
                notesViewModel.changeTitleInput(newTitle: String(title.prefix(limit)))
                print(notesViewModel.titleInput.value!)
            }
            else {
                notesViewModel.changeTitleInput(newTitle: String(title.prefix(limit)))
            }
        }
    }
    
    init(){
        start()
    }
    
    func start(){
        self.notesViewModel.noteColor.watch(block : { newColor in
            self.chosenColor = newColor! as String
        })
    }
    
    
}


struct AddNewNoteView : View{
    
    @State var content = ""
    @ObservedObject var stateObject = AddNewNoteViewHelper()
    
    init() {
          UITextView.appearance().backgroundColor = .clear
      }
    
    var body : some View{
        
        let contentBinding = Binding<String>(get: {
            self.content
        }, set: {
            self.content = $0
            stateObject.notesViewModel.changeContentInput(newContent: self.content)
        })
        
        ZStack{
            RoundedRectangle(cornerRadius: 20,style: .continuous)
                .fill(NoteColor.init(color: stateObject.chosenColor).getColor())
            
            GeometryReader{ geo in
                VStack{
                    TextField("Note title",text: $stateObject.title)
                        .font(.title)
                        .padding(.horizontal)
                        .padding(.vertical,10)
                    Divider()
                    TextEditor(text : contentBinding)
                        .background(.clear)
                        .frame(height: geo.size.height * 0.6)
                        .textFieldStyle(PlainTextFieldStyle())
                        .padding(.horizontal,10)
                       
                    Divider()
                    BallsRow(
                                chosenColor: stateObject.chosenColor,
                                pickColor : { color in stateObject.notesViewModel.changeNoteColor(newColor: color)}
                            )
                    Spacer()
                    HStack{
                        Text("Button1")
                        Text("Button2")
                        Button("Add note"){
                            stateObject.notesViewModel.addNote(providedId: nil)
                        }
                    }
                    .frame(height: geo.size.height * 0.2)
                }
            }
            
        }.padding()
        
    }
    
}

struct AddNewNotePreview : PreviewProvider{
    
    static var previews: some View {
        AddNewNoteView()
    }
    
}
