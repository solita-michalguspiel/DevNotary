//
//  Extensions.swift
//  iosApp
//
//  Created by Michal Guspiel on 25.5.2022.
//

import Foundation
import SwiftUI
import shared


extension Color{
    static let background = Color("Background")
    static let icon = Color("Icons")
    static let buttons = Color("Buttons")
    static let text = Color("Text")
    static let systemBackground = Color(uiColor: .systemBackground)

    static let red = Color("Red")
    static let blue = Color("Blue")
    static let green = Color("Green")
    static let pink = Color("Pink")
    static let yellow = Color("Yellow")
    static let white = Color("White")
}

extension Closeable{
    func addToListenerList(list: inout Array<Closeable>){
        list.append(self)
    }
}
