//
//  iosAppApp.swift
//  iosApp
//
//  Created by Michal Guspiel on 20.5.2022.
//

import SwiftUI

@main
struct iosAppApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate
    var body: some Scene {
        WindowGroup {
            SignInScreen()
        }
    }
}
