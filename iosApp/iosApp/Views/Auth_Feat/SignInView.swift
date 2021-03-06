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
    
    @Published var sendLinkState : Any = ResponseEmpty.self
    @Published var timerCount : Int = 0
    
    init(){
        start()
    }
    
    func start(){
        authViewModel.watch(authViewModel.sendLinkState,block: {  state in
            self.sendLinkState = state!
        })
        
        authViewModel.watch(authViewModel.resendEmailTimer,block:{ timer in
            self.timerCount = ( timer.map({KotlinInt.init(int: Int32(truncating: $0 as! NSNumber))}) ) as! Int
        })
        
    }
    func isSendLinkButtonDisabled() -> Bool{
        return (!(timerCount == 0) || sendLinkState is ResponseLoading)
    }
}

struct SignInView: View {
    @StateObject var stateObject = SignInViewStateObject()
    @State var email = ""
    
    var body: some View {
        let binding = Binding<String>(get: {
            self.email
        }, set: {
            self.email = $0
            stateObject.authViewModel.changeEmailAddress(newEmailAddress: self.email)
        })
        
        return NavigationView{
            let buttonColor = stateObject.isSendLinkButtonDisabled() ? Color.gray : Color.buttons
            VStack{
             
                Text("Dev notary")
                    .font(/*@START_MENU_TOKEN@*/.largeTitle/*@END_MENU_TOKEN@*/)
                    .fontWeight(.bold)
                    .padding(.top, 30.0)
                Spacer()
                VStack{
                    Text("Sign in with email").font(.callout)
                    TextField("Enter email address", text: binding)
                        .textInputAutocapitalization(.never)
                        .disableAutocorrection(true)
                        .padding(.horizontal, 30.0)
                        .textFieldStyle(RoundedBorderTextFieldStyle())
                }
                switch(stateObject.sendLinkState as Any){
                case _ as ResponseLoading:
                    ProgressView()
                case _ as ResponseError:
                    Text("Incorrect e-mail address.")
                        .foregroundColor(.red)
                        .font(.caption)
                case _ as ResponseSuccess<AnyObject>:
                    Text("Link sent!").font(.caption)
                default:
                    EmptyView()
                }
                
                Spacer()
                Button(action : {
                    stateObject.authViewModel.sendEmailLink()
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
                }.disabled(stateObject.isSendLinkButtonDisabled())
                
                if stateObject.timerCount != 0{
                    Text("Send again in: " + String(stateObject.timerCount) + " seconds.")
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
