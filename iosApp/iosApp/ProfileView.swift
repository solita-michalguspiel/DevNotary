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

    
    init(){
        start()
    }
    
    func start(){
        authViewModel.userState.watch{state in
              let response = state! as Any
              self.userState = watchResponse(response: response)
        }
    }
    
}


struct ProfileView : View{
    
    @StateObject var stateObject = ProfileViewStateObject()
    @Environment(\.presentationMode) var presentationMode: Binding<PresentationMode>

    
    var body : some View {
        if(stateObject.userState.isKind!(of: ResponseSuccess<User>.self)){
            let user = (stateObject.userState as! ResponseSuccess<User>)
            VStack(spacing : 30){
                
                Text(user.data!.userEmail)
                
                Button("Sign out"){
                    stateObject.authViewModel.signOut()
                    presentationMode.wrappedValue.dismiss()
                }
            }.navigationBarBackButtonHidden(true)
        }
    else{
        Text("Loading...").onAppear(){
            stateObject.authViewModel.getCurrentUserDocument()
        }
    }
    }


}
