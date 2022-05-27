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
        authViewModel.userState.watch{state in
              let response = state! as Any
              self.userState = watchResponse(response: response)
        }
        authViewModel.userAuthState.watch{state in
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
        }
    }
    
}

struct ProfileView : View{
    
    @StateObject var stateObject = ProfileViewStateObject()
    @Environment(\.presentationMode) var presentationMode: Binding<PresentationMode>

    var body : some View {
        
    return TabView{
        Text("This is going to show notes")
        .tabItem{
            Image(systemName: "house.fill")
            Text("Notes")
        }
        
        
        ZStack{
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
        }.tabItem{
            Image(systemName: "person.fill")
            Text("Profile ")
        }
    }.navigationBarBackButtonHidden(true)
    }
}
