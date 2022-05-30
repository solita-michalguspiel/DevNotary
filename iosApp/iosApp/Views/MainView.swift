//
//  MainView.swift
//  iosApp
//
//  Created by Michal Guspiel on 30.5.2022.
//

import Foundation
import SwiftUI


struct MainView : View{
    
    var body : some View{
    
    return TabView{
        ProfileView().tabItem{
                Image(systemName: "person.fill")
                Text("Profile ")
        }
        NotesView().tabItem{
            Image(systemName: "house.fill")
            Text("Notes")
        }
    }
}
}
