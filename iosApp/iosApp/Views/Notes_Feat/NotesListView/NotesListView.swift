//
//  NotesView.swift
//  iosApp
//
//  Created by Michal Guspiel on 30.5.2022.
//

import Foundation
import SwiftUI
import shared


class NotesData : ObservableObject{
    
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

struct NotesListView : View{
    @State var isActive : Bool = false
    @StateObject var notesData = NotesData()
    
    var body : some View{
        
        return ZStack{
            NavigationLink(destination: MainView(selectedTab : 2),isActive: self.$isActive){
                EmptyView()
            }
            ScrollView{
                VStack{
                    ForEach(notesData.notes, id: \.self){ note in
                        NotePreview(note: note)
                    }
                }
            }
            VStack{
                Spacer()
                HStack{
                    Spacer()
                    NavigationLink(
                        destination: NoteInteractionView(editedNote: nil)){
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
                notesData.viewModel.getSharedNotes()
            }
    }
}

struct NoteView_Previews : PreviewProvider{
    static var previews : some View  {
        NotesListView()
    }
}
