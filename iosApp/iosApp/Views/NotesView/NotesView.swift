//
//  NotesView.swift
//  iosApp
//
//  Created by Michal Guspiel on 30.5.2022.
//

import Foundation
import SwiftUI
import shared


struct NotesView : View{
    
    static let testNote = Note.init(noteId: "testID", ownerUserId: "59017250192", title:"Database plan", content: "Some random SQL database plan, loreum ipseum bla la test loreum", dateTime: "2022-05-17T08:08:08.715Z", color: "pink")
    
    var body : some View{
        
        
       return ZStack{
           
           ScrollView{
               LazyVStack{
                   ForEach(0 ... 20, id: \.self){ _ in
                       NotePreview(note: NotesView.testNote)
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
