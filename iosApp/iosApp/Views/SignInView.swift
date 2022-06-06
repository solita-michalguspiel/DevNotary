//
//  SignInScreen.swift
//  iosApp
//
//  Created by Michal Guspiel on 24.5.2022.
//

import Foundation
import SwiftUI
import shared
import Combine

class SignInViewStateObject : ObservableObject{
    
    var authViewModel = iosDI().getAuthViewModel()
    
    @Published var emailAddress : String = ""
        
    @Published var sendLinkState : AnyObject = ResponseEmpty()
    
    @Published var userAuthState : AnyObject = ResponseEmpty()
    
    @Published var userState : AnyObject = ResponseEmpty()
    
    @Published var shouldNavigate : Bool = false
    
    init(){
        start()
    }
    
    func start(){
        authViewModel.watch(authViewModel.sendLinkState,block: {  state in
            let response = state! as Any
            self.sendLinkState = watchResponse(response: response)
        })
        
        authViewModel.watch(authViewModel.userState,block: {state in
              let response = state! as Any
              self.userState = watchResponse(response: response)
        })
        
        authViewModel.watch(authViewModel.userAuthState,block: {state in
            let response = state! as Any
            self.userAuthState = watchResponse(response: response)
            
            if(self.userAuthState.isKind(of: ResponseSuccess<AnyObject>.self)){
                let userAuthState = self.userAuthState as! ResponseSuccess<KotlinBoolean>
                if(userAuthState.data == true){
                    print("True, let's log in")
                    self.shouldNavigate = true
                }
                else{
                    print("False, means signed out!")
                }
            }
        })
        
    }
    
}

struct SignInView: View {
    @StateObject var stateObject = SignInViewStateObject()
    @EnvironmentObject var appState: AppState
    @State var email = ""
    @State var signInState : AnyObject? = nil
   
    var body: some View {
        let binding = Binding<String>(get: {
            self.email
        }, set: {
            self.email = $0
            stateObject.authViewModel.changeEmailAddress(newEmailAddress: self.email)
        })
        
        
        return NavigationView{
            VStack{
                NavigationLink(destination: MainView(selectedTab : appState.selectedTab)
                               ,isActive: $stateObject.shouldNavigate){
                    EmptyView()
                }
                Text("Dev notary")
                    .font(/*@START_MENU_TOKEN@*/.largeTitle/*@END_MENU_TOKEN@*/)
                    .fontWeight(.bold)
                    .padding(.top, 30.0)
                Spacer()
                VStack{
                    Text("Sign in with email").font(.callout)
                    TextField("Enter email address", text: binding)
                        .padding(.horizontal, 30.0).textFieldStyle(RoundedBorderTextFieldStyle())
                }
                Spacer()
                Button(action : {
                    stateObject.authViewModel.sendEmailLink()
                }){
                    Text("Get email")
                        .fontWeight(.bold)
                        .font(.body)
                        .padding(10)
                        .background(Color.buttons)
                        .cornerRadius(20)
                        .foregroundColor(.white)
                        .padding(5)
                        .overlay(
                            RoundedRectangle(cornerRadius: 25)
                                .stroke(Color.buttons, lineWidth: 3)
                        ).padding(.bottom,3.0)
                }
                
            }.onReceive(self.appState.$moveToDashboard){ moveToDashBoard in
                if moveToDashBoard{
                    print("Move to dashboard : \(moveToDashBoard)")
                    self.appState.moveToDashboard = false
                }
            }
        }.onAppear(){
            if(stateObject.authViewModel.isUserAuthenticated){
                print("Already authenticated!")
                stateObject.shouldNavigate = true
            }
        }
    }
}

struct SignInView_Previews: PreviewProvider {
    static var previews: some View {
        SignInView()
    }
}
