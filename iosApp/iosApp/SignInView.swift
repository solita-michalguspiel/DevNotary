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


class AuthViewModelHelper : ObservableObject{
    
    var authViewModel = iosDI().getAuthViewModel()
    
    @Published var emailAddress : String = ""
    
    @Published var timerCount : Int = 0
    
    @Published var sendLinkState : AnyObject = ResponseEmpty()
    
    init(){
        start()
    }
    
    func start(){
        
        authViewModel.resendEmailTimer.watch{ timer in
            self.timerCount = ( timer.map({KotlinInt.init(int: Int32(truncating: $0))}) ) as! Int
        }
        
        authViewModel.sendLinkState.watch{  state in
            let response = state! as Any
            switch response{
            case _ as ResponseEmpty :
                self.sendLinkState = ResponseEmpty()
            case _ as ResponseError :
                self.sendLinkState = ResponseError.self
            case _ as ResponseLoading :
                self.sendLinkState = ResponseLoading()
            case _ as ResponseSuccess<AnyObject> :
                self.sendLinkState = (response as! ResponseSuccess<AnyObject>)
            default : print("It's something else")
            }
            
            print(state! as Response)
        }
        
    }
    
    func isSendLinkButtonDisabled() -> Bool{
        print("Timer count : " + String(timerCount != 0))
        print("is Loading:  : " + String(sendLinkState.isKind(of: ResponseLoading.self)))
        return (timerCount != 0 || sendLinkState.isKind(of: ResponseLoading.self))
    }
    
}

struct SignInView: View {
    @ObservedObject var authViewModel = AuthViewModelHelper()
    @State var email = ""
    @State var navigateToProfileScreen = false
    
    var body: some View {
        
        let binding = Binding<String>(get: {
            self.email
        }, set: {
            self.email = $0
            authViewModel.authViewModel.changeEmailAddress(newEmailAddress: self.email)
        })
        
        var buttonColor: Color {
            return !authViewModel.isSendLinkButtonDisabled() ? Color.buttons : .gray
           }
        
        return NavigationView{
            VStack{
                NavigationLink(destination: ProfileView(),isActive: $navigateToProfileScreen){
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
                      authViewModel.authViewModel.sendEmailLink()
                }){
                    Text("Get email")
                        .fontWeight(.bold)
                        .font(.body)
                        .padding(10)
                        .background(buttonColor)
                        .cornerRadius(20)
                        .foregroundColor(.white)
                        .padding(5)
                        .overlay(
                            RoundedRectangle(cornerRadius: 25)
                                .stroke(buttonColor, lineWidth: 3)
                        ).padding(.bottom,3.0)
                }.disabled(authViewModel.isSendLinkButtonDisabled())
                
                
                if authViewModel.timerCount != 0{
                    Text("Send again in: " + String(authViewModel.timerCount) + " seconds.")
                        .font(.callout)
                        .foregroundColor(.gray)
                        .padding(.bottom)
                }
            }
        }
    }
}

struct SignInView_Previews: PreviewProvider {
    static var previews: some View {
        SignInView()
    }
}
