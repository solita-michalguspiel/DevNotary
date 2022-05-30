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
    
    static let testNote = Note.init(noteId: "testID", ownerUserId: "59017250192", title:"Database plan", content: "Some random SQL database plan, loreum ipseum bla la test loreum", dateTime: "2015.12.30 16:35", color: "blue")
    
    var body : some View{
        
        
       return ZStack{
            
           ScrollView{
               LazyVStack{
                   ForEach(0 ... 20, id: \.self){ _ in
                       NotePreview(note: NotesView.testNote)
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
