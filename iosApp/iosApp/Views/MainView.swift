//
//  MainView.swift
//  iosApp
//
//  Created by Michal Guspiel on 30.5.2022.
//

import Foundation
import SwiftUI
import shared

struct MainView : View{
    @State var selection : Int
    
    let sortOptions = [SortOptions.byNameAsc,SortOptions.byNameDesc,SortOptions.byDateAsc,SortOptions.byDateDesc]
    var viewModel = iosDI().getNotesListViewModel()
    
    var body : some View{
        let navigationTitle = selection == 1 ? "Profile" : "Notes"
        return GeometryReader { geometry in
            VStack {
                if(selection == 1){
                    ProfileView().frame(height : geometry.size.height * 0.93 )
                }else{NotesListView()
                        .frame(height : (geometry.size.height * 0.93))
                               }
                HStack{
                    Spacer()
                    VStack{
                        Image(systemName: "person.fill").foregroundColor(
                            selection == 1 ? Color.blue : Color.gray
                        )
                        Text("Profile").font(.caption).foregroundColor(
                            selection == 1 ? Color.blue : Color.gray
                        )
                    }.onTapGesture {
                        selection = 1
                    }
                    .padding(.horizontal)
                    Spacer()
                    VStack{
                        Image(systemName: "house.fill").foregroundColor(
                            selection == 2 ? Color.blue : Color.gray
                        )
                        Text("Notes").font(.caption).foregroundColor(
                            selection == 2 ? Color.blue : Color.gray
                        )
                    }.onTapGesture {
                        selection = 2
                    }
                    .padding(.horizontal)
                    Spacer()
                }.frame(width: geometry.size.width, height: geometry.size.height * 0.07)
            }.edgesIgnoringSafeArea(.bottom)
                .background(Color.background)
        }.navigationBarBackButtonHidden(true)
            .navigationTitle(navigationTitle)
            .toolbar(){
                           ToolbarItem(placement: .navigationBarTrailing){
                               if(selection == 2){
                                   HStack{
                                       Text("Sort")
                                       Image(systemName: "arrow.up.arrow.down.square.fill")
                                   }
                                   .contextMenu{
                                       ForEach(sortOptions,id: \.self){ eachSort in
                                           Button(action: {
                                               viewModel.changeSortSelection(sort: eachSort.sort)
                                           }){
                                               Text(eachSort.sortName)
                                           }
                                       }
                                   }
                               }else{EmptyView()}
                           }
    }
}
}
