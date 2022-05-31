//
//  NotePreview.swift
//  iosApp
//
//  Created by Michal Guspiel on 30.5.2022.
//

import SwiftUI
import shared

class NotePreviewHelper : ObservableObject{
    
    let notesViewModel = iosDI().getNotesViewModel()
    
    
}

struct NotePreview: View {
    let note: Note
    let notePreviewHelper = NotePreviewHelper()
    
    var body: some View {
    
        ZStack{
            RoundedRectangle(cornerRadius: 20, style: RoundedCornerStyle.continuous)
                .fill(NoteColor.init(color: note.color).getColor())
            VStack{
                HStack{
                    Text(note.title).font(.headline).bold()
                    Spacer()
                    if note.ownerUserId != nil {
                        Image(systemName : "folder.fill.badge.person.crop")
                        }
                }
                .padding(.horizontal, 10)
                .padding(.vertical, 5)
                Text(note.content)
                    .frame(maxWidth: .infinity, alignment : .leading)
                .font(.body)
                .lineLimit(1)
                .padding(.horizontal, 10)
            
                HStack{
                    Text(notePreviewHelper.notesViewModel.formatDateTime(date: note.dateTime)).font(.caption)
                    Spacer()
                    Button(action: {
                        print("View details!")
                    }){
                        Text("View details")
                            .fontWeight(.bold)
                            .foregroundColor(Color.black)
                    }
                }
                .padding(.horizontal, 10)
                .padding(.vertical, 10)
            }.padding(10)
        }
        .padding(.vertical,5)
        .padding(.horizontal)
        .frame(
              minWidth: 0,
              maxWidth: .infinity,
              minHeight: 0,
              maxHeight: .infinity,
              alignment: .topLeading
            )
    }
}

struct NotePreview_Previews: PreviewProvider {
    
    static let testNote = Note.init(noteId: "testID", ownerUserId: "59017250192", title:"Database plan", content: "Some random SQL database plan, loreum ipseum bla la test loreum", dateTime: "2015.12.30 16:35", color: "blue")
    
    static var previews: some View {
        NotePreview(note: testNote )
    }
}
