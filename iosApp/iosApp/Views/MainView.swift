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
    @State var selectedTab : Int
    @State private var searchText = ""
    
    let sortOptions = [SortOptions.byNameAsc,SortOptions.byNameDesc,SortOptions.byDateAsc,SortOptions.byDateDesc]
    var viewModel = iosDI().getNotesListViewModel()
    
    var body : some View{
        let navigationTitle = selectedTab == 1 ? "Profile" : "Notes"
        return TabView(selection: $selectedTab){
            ProfileView().tabItem{
                Image(systemName: "person.fill")
                Text("Profile ")
            }.tag(1)
            NotesListView()
                .tabItem{
                    Image(systemName: "house.fill")
                    Text("Notes")
                }.tag(2)
        }.navigationTitle(navigationTitle)
            .toolbar(){
                ToolbarItem(placement: .navigationBarTrailing){
                    if(selectedTab == 2){
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
