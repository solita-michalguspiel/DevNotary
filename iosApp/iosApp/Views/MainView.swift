//
//  MainView.swift
//  iosApp
//
//  Created by Michal Guspiel on 30.5.2022.
//

import Foundation
import SwiftUI

struct MainView : View{
    @State var selectedTab : Int
    
    var body : some View{
    
        return TabView(selection: $selectedTab){
        ProfileView().tabItem{
                Image(systemName: "person.fill")
                Text("Profile ")
        }.tag(1).onAppear(perform: {
            print("Selected tab : \(selectedTab)")
        })
        NotesView().tabItem{
            Image(systemName: "house.fill")
            Text("Notes")
        }.tag(2).onAppear(perform: {
            print("Selected tab : \(selectedTab)")
        })
    }
}
}
