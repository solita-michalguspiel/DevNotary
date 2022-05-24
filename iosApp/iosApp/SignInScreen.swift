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
            Spacer()
            Button("Click me"){
                authViewModel.authViewModel.sendEmailLink()
            }
        }

    }
}
