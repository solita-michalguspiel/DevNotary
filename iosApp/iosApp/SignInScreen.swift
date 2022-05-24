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
    
    init(){
        start()
    }
    
    func start(){
        
        authViewModel.resendEmailTimer.watch{ timer in
            self.timerCount = ( timer.map({KotlinInt.init(int: Int32(truncating: $0))}) ) as! Int
        }
        
    }
    
}

struct SignInScreen: View {
    @ObservedObject var authViewModel = AuthViewModelHelper()
    
    
    @State var email = ""
    
    var body: some View {
        
        let binding = Binding<String>(get: {
                    self.email
                }, set: {
                    self.email = $0
                    authViewModel.authViewModel.changeEmailAddress(newEmailAddress: self.email)
                })
        
        
        
        return VStack{
            Spacer()
            Text("Dev notary")
            Spacer()
            TextField(/*@START_MENU_TOKEN@*/"Placeholder"/*@END_MENU_TOKEN@*/, text: binding)
            Text("Loading :" + String(authViewModel.timerCount))
            Spacer()
            Button("Click me"){
                authViewModel.authViewModel.sendEmailLink()
            }
        }

    }
}
