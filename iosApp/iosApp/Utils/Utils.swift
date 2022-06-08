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
    print("Handling incoming dynamic link!")
    guard let url = dynamicLink.url else{
        print("That's weird, my dynamic link object has no url")
        return
    }
    authViewModel.signInWithLink(intent: url.absoluteString)
    print("Your incoming link has a parameter \(url.absoluteString)")
}
