//
//  AppDelegate.swift
//  iosApp
//
//  Created by Michal Guspiel on 23.5.2022.
//

import UIKit
import FirebaseCore




class AppDelegate: UIResponder, UIApplicationDelegate {

  var window: UIWindow?

  func application(_ application: UIApplication,

    didFinishLaunchingWithOptions launchOptions:

                   [UIApplication.LaunchOptionsKey: Any]?) -> Bool {

    FirebaseApp.configure()

    return true

  }

}
