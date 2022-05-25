//
//  ProfileView.swift
//  iosApp
//
//  Created by Michal Guspiel on 25.5.2022.
//

import Foundation
import SwiftUI


struct ProfileView : View{
    
    @State var navigationDestination : String? = nil
    @Environment(\.presentationMode) var presentationMode: Binding<PresentationMode>
    var body : some View {
        
            VStack(spacing : 30){
                NavigationLink(destination : SignInView(), tag : Constants.SIGN_IN_SCREEN,selection : $navigationDestination){EmptyView()}
                
                Text("test@gmail.com - fake name")
                
                Button("Sign out"){
                    self.presentationMode.wrappedValue.dismiss()
                }
            }.navigationBarBackButtonHidden(true)
        }
}
