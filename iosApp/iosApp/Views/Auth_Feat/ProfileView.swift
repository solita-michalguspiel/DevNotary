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

    @Published var userState : AnyObject = ResponseEmpty()
    
    @Published var userAuthState : AnyObject = ResponseEmpty()
    
    @Published var shouldPopBackStack : Bool = false


    init(){
        start()
    }
    
    func start(){
        authViewModel.watch(authViewModel.userState,block : {state in
              let response = state! as Any
              self.userState = watchResponse(response: response)
        })
        authViewModel.watch(authViewModel.userAuthState, block:{state in
            let response = state! as Any
            self.userAuthState = watchResponse(response: response)
            
            if(self.userAuthState.isKind(of: ResponseSuccess<AnyObject>.self)){
                let userAuthState = self.userAuthState as! ResponseSuccess<KotlinBoolean>
                if(userAuthState.data == true){
                   print("True, means signed in")
                }
                else{
                    print("False, means signed out!")
                    self.shouldPopBackStack = true
                }
            }
        })
    }
    
}

struct ProfileView : View{
    
    @StateObject var stateObject = ProfileViewStateObject()
    @Environment(\.presentationMode) var presentationMode: Binding<PresentationMode>
    @EnvironmentObject var appState: AppState


    var body : some View {
        
    return ZStack{
        if(stateObject.userState.isKind!(of: ResponseSuccess<User>.self)){
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
        if stateObject.shouldPopBackStack {
            Text("").onAppear(){ presentationMode.wrappedValue.dismiss()}
            }
        }.navigationBarBackButtonHidden(true)
            .onAppear{
                appState.selectedTab = 1
            }
    }
}
    

