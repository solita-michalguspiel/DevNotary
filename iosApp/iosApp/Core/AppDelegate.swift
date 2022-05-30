//
//  AppDelegate.swift
//  iosApp
//
//  Created by Michal Guspiel on 23.5.2022.
//

import UIKit
import FirebaseCore
import FirebaseAuth
import shared
import Firebase
import FirebaseDynamicLinks

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

  func application(_ application: UIApplication,

    didFinishLaunchingWithOptions launchOptions:
                   [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
      print("First delegate func")
    FirebaseApp.configure()
      
      return true
  }
    

    func application(_ app: UIApplication, open url: URL, options: [UIApplication.OpenURLOptionsKey : Any] = [:]) -> Bool {
        print("I have received an URL through a custom scheme! \(url.absoluteString)")
        if let dynamicLink = DynamicLinks.dynamicLinks().dynamicLink(fromCustomSchemeURL: url){
            handleIncomingDynamicLink(dynamicLink)
            return true
        }
        else{
            return false
        }
    }
    
    
}
