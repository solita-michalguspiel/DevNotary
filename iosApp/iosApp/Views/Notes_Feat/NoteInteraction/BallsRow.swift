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
                Spacer()
                ForEach(AvailableNoteColors().getAvailableColors()){ color in
                    let isChosen = isChosen(ballColorName: color.colorName, chosenColorName: chosenColor)
                    Ball.init(color: color,isChosen: isChosen)
                        .onTapGesture {
                            pickColor(color.colorName)}
                }
                Spacer()
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
    let ballWidthPercent = 0.12
    
    init(color: AvailableNoteColor ,isChosen: Bool){
        self.color = color
        self.isChosen = isChosen
        strokeColor = isChosen ? Color.black : Color.gray
    }
    var body : some View{
        let ringSize : CGFloat = isChosen ? 50 : 48
        ZStack{
            Circle().fill(strokeColor)
                .frame(width : ringSize, height: ringSize)
            Circle().fill(self.color.color)
                .frame(width: 45, height: 45)
        }
    }
}
