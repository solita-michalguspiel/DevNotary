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

class AvailableNoteColor{
    let color : Color
    let colorName : String
    
    init(color: Color, colorName : String){
        self.color = color
        self.colorName = colorName
    }
}

struct AvailableNoteColors{
    let RED     : AvailableNoteColor  = AvailableNoteColor.init(color: Color.red, colorName: "red")
    let BLUE    : AvailableNoteColor  = AvailableNoteColor.init(color: Color.blue, colorName: "blue")
    let GREEN   : AvailableNoteColor  = AvailableNoteColor.init(color: Color.green, colorName: "green")
    let PINK    : AvailableNoteColor  = AvailableNoteColor.init(color: Color.pink, colorName: "pink")
    let YELLOW  : AvailableNoteColor  = AvailableNoteColor.init(color: Color.yellow, colorName: "yellow")
    let WHITE   : AvailableNoteColor  = AvailableNoteColor.init(color: Color.white, colorName: "white")
}

func getAvailableColors() -> Array<AvailableNoteColor>{
    let colors = AvailableNoteColors.init()
    
    return [colors.RED,colors.BLUE,colors.GREEN,colors.PINK,colors.YELLOW,colors.WHITE]
}
