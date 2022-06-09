//
//  NotePreview.swift
//  iosApp
//
//  Created by Michal Guspiel on 30.5.2022.
//

import SwiftUI
import shared

struct NotePreview: View {
    let note: Note
    let viewModel = iosDI().getNotesDetailViewModel()
    @State var selection : Note? = nil
    
    var body: some View {
        
     NavigationLink(destination : NoteDetailsView(note: note), tag: note, selection : $selection){
          EmptyView()
        }
        
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
                    Text(viewModel.formatDateTime(date: note.dateTime)).font(.caption)
                    Spacer()
                    Button(action: {
                        selection = note
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
        .frame(
            minWidth: 0,
            maxWidth: .infinity,
            minHeight: 0,
            maxHeight: .infinity,
            alignment: .topLeading
        )
    }
}
