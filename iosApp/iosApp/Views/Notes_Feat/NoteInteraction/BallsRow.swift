//
//  BallsRow.swift
//  iosApp
//
//  Created by Michal Guspiel on 31.5.2022.
//

import SwiftUI
import shared


struct BallsRow: View {
    
    let chosenColor : String
    let pickColor : (String) -> ()
    
    init(chosenColor : String, pickColor : @escaping (String) -> ()){
        self.chosenColor = chosenColor
        self.pickColor = pickColor
    }
    
    var body: some View {
        HStack{
            ForEach(AvailableNoteColors().getAvailableColors()){ color in
                let isChosen = isChosen(ballColorName: color.colorName, chosenColorName: chosenColor)
                Ball.init(color: color,isChosen: isChosen)
                    .onTapGesture {
                    pickColor(color.colorName)}
                }
            }
        }
    
    func isChosen(ballColorName: String, chosenColorName : String) -> Bool{
        if ballColorName == chosenColorName {
            return true
        }
        else {
            return false
        }
    }
    
}


struct Ball : View{
    let isChosen : Bool
    let color : AvailableNoteColor
    var strokeColor : Color
    var ringSize : CGFloat
    
    init(color: AvailableNoteColor ,isChosen: Bool){
        self.color = color
        self.isChosen = isChosen
        strokeColor = isChosen ? Color.black : Color.gray
        ringSize = isChosen ? 60 : 55
    }
    var body : some View{
        ZStack{
        Circle().fill(strokeColor)
                .frame(width: ringSize,height: ringSize)
            Circle().fill(self.color.color)
            .frame(width: 50, height: 50)
        }
    }
}

