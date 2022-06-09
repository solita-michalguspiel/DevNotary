//
//  MainView.swift
//  iosApp
//
//  Created by Michal Guspiel on 30.5.2022.
//

import Foundation
import SwiftUI
import shared

class MainViewModel: ObservableObject{
    var authViewModel = iosDI().getAuthViewModel()
    
    init(){
        start()
    }
    
    @Published var isUserSignedIn = false
    func start(){
        isUserSignedIn = authViewModel.isUserAuthenticated
        authViewModel.watch(authViewModel.userAuthState,block: {state in
            
            if(state! is ResponseSuccess<AnyObject>){
                let userAuthState = state! as! ResponseSuccess<KotlinBoolean>
                self.isUserSignedIn = userAuthState.data as! Bool
            }
        })
    }
}

struct MainView : View{
    @EnvironmentObject var appState: AppState
    @ObservedObject var viewModel = MainViewModel()
    @State private var notesListViewUUID = UUID()
    
    var body : some View{
            if(viewModel.isUserSignedIn){
                TabView(){
                    ProfileView()
                        .tabItem{
                            Image(systemName: "person.fill")
                            Text("Profile ")
                        }
                    NavigationView{
                        NotesListView().background(Color.background)
                            .id(notesListViewUUID)
                            .navigationTitle("Notes")
                            .onChange(of: appState.moveToDashboard) { _ in
                                notesListViewUUID = UUID()
                            }
                    }.navigationViewStyle(.stack)
                        .tabItem{
                            Image(systemName: "house.fill")
                            Text("Notes")
                        }
                }
            }else{
                SignInView()
            }
    }
}
