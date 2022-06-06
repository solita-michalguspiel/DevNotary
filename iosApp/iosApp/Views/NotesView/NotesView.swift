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
    
    let viewModel = iosDI().getNotesListViewModel()

    @Published var notes : Array<Note> = Array()
    
    init(){
        start()
        viewModel.listenToNoteListChanges()
    }

    func start() {
        viewModel.watch(viewModel.notes, block: {newNotes in
            let newNotesAsArray = newNotes as! Array<Note>
            self.notes = newNotesAsArray
        })
    }
    
}

struct NotesView : View{
    @State var isActive : Bool = false
    
    @StateObject var stateObject = NotesViewHelper()
        
    var body : some View{
        
       return ZStack{
           
           NavigationLink(destination: MainView(selectedTab : 2),isActive: self.$isActive){
               EmptyView()
           }
           
           ScrollView{
               VStack{
                   ForEach(stateObject.notes, id: \.self){ note in
                       NotePreview(note: note)
                   }
               }
           }
           
           VStack{
               Spacer()
               HStack{
                   Spacer()
                   NavigationLink(destination: AddAndEditNoteView(editedNote: nil)){
                                          Image(systemName: "plus")
                                              .font(.largeTitle)
                                              .frame(width: 70, height: 70)
                                              .background(Color.buttons)
                                              .clipShape(Circle())
                                              .foregroundColor(.white)
                                              .padding()
                    }.isDetailLink(false)
                   }
                  
               }
       }.navigationBarBackButtonHidden(true)
            .onAppear{
                stateObject.viewModel.getSharedNotes()
            }
            .onDisappear{
                print("Notes view on disappear!")
            }
        }
    }


struct NoteView_Previews : PreviewProvider{
    static var previews : some View  {
        NotesView()
    }
}
