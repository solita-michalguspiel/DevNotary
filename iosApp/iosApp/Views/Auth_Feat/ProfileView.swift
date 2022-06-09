//
//  ProfileView.swift
//  iosApp
//
//  Created by Michal Guspiel on 25.5.2022.
//

import Foundation
import SwiftUI
import shared


class ProfileViewStateObject : ObservableObject{
    
    var authViewModel = iosDI().getAuthViewModel()
    
    @Published var userState : Any = ResponseEmpty.self
    
    init(){
        start()
    }
    
    func start(){
        authViewModel.watch(authViewModel.userState,block : {state in
            self.userState = state!
        })
    }
}

struct ProfileView : View{
    
    @StateObject var stateObject = ProfileViewStateObject()
    @Environment(\.presentationMode) var presentationMode: Binding<PresentationMode>
    @EnvironmentObject var appState: AppState
    
    var body : some View {
        
        return ZStack{
            if(stateObject.userState is ResponseSuccess<User>){
                let user = (stateObject.userState as! ResponseSuccess<User>)
                VStack(spacing : 30){
                    Text("Hello " + user.data!.userEmail)
                    Button("Sign out"){
                        stateObject.authViewModel.signOut()
                    }
                }
            }
            else{
                Text("Loading...").onAppear(){
                    stateObject.authViewModel.getCurrentUserDocument()
                }
            }
        }
        .navigationBarBackButtonHidden(true)
        .onAppear{
            appState.selectedTab = 1
        }
    }
}
