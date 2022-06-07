//
//  NoteColor.swift
//  iosApp
//
//  Created by Michal Guspiel on 30.5.2022.
//

import Foundation
import SwiftUI

class NoteColor{
    let color : String
    
    init(color : String){
        self.color = color
    }
    
    func getColor() -> Color{
        switch(self.color) {
        case "red": return Color.red
        case "blue" : return Color.blue
        case "green" : return Color.green
        case "pink" : return Color.pink
        case "yellow" : return Color.yellow
        case "white" : return  Color.white
        default : return Color.white
        }
    }
}
