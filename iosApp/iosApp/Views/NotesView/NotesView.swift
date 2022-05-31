//
//  NotesView.swift
//  iosApp
//
//  Created by Michal Guspiel on 30.5.2022.
//

import Foundation
import SwiftUI
import shared


class NotesViewHelper : ObservableObject{
    
    let notesViewModel = iosDI().getNotesViewModel()

    @Published var notes : Array<Note> = Array()
    
    init(){
        start()
        notesViewModel.listenToNoteListChanges()
    }

    func start() {
        notesViewModel.notes.watch(block:{newNotes in
            for note in newNotes as! Array<Note> {
                print("Watching notes:")
                print(note.title)
            }
            let newNotesAsArray = newNotes as! Array<Note>
            self.notes = newNotesAsArray
        })
    }
    
}

struct NotesView : View{
    
    static let testNote = Note.init(noteId: "testID", ownerUserId: "59017250192", title:"Database plan", content: "Some random SQL database plan, loreum ipseum bla la test loreum", dateTime: "2022-05-17T08:08:08.715Z", color: "pink")
    @StateObject var stateObject = NotesViewHelper()
    
    var body : some View{
        
        
       return ZStack{
           
           ScrollView{
               LazyVStack{
                   ForEach(stateObject.notes, id: \.self){ note in
                       NotePreview(note: note)
                   }
               }
           }
           
           VStack{
               Spacer()
               HStack{
                   Spacer()
                   NavigationLink(destination: AddNewNoteView()){
                           Image(systemName: "plus")
                               .font(.largeTitle)
                               .frame(width: 70, height: 70)
                               .background(Color.buttons)
                               .clipShape(Circle())
                               .foregroundColor(.white)
                               .padding()
                       }
                   }
                  
               }
           }
           
        }
    }


struct NoteView_Previews : PreviewProvider{
    static var previews : some View  {
        NotesView()
    }
}
