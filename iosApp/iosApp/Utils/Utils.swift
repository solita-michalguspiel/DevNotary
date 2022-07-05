//
//  Utils.swift
//  iosApp
//
//  Created by Michal Guspiel on 27.5.2022.
//

import Foundation
import FirebaseDynamicLinks
import shared

func handleIncomingDynamicLink(_ dynamicLink : DynamicLink){
    let authViewModel = iosDI().getAuthViewModel()
    guard let url = dynamicLink.url else{
        return
    }
    authViewModel.signInWithLink(intent: url.absoluteString)
}
