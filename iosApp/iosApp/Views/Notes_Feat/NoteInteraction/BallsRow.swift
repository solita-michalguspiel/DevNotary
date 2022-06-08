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
        GeometryReader{ geo in
            HStack{
                Spacer()
                ForEach(AvailableNoteColors().getAvailableColors()){ color in
                    let isChosen = isChosen(ballColorName: color.colorName, chosenColorName: chosenColor)
                    Ball.init(color: color,isChosen: isChosen,geo: geo)
                        .onTapGesture {
                            pickColor(color.colorName)}
                }
                Spacer()
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
    let geo : GeometryProxy
    var strokeColor : Color
    let ballWidthPercent = 0.12
    
    init(color: AvailableNoteColor ,isChosen: Bool,geo : GeometryProxy){
        self.color = color
        self.isChosen = isChosen
        self.geo = geo
        strokeColor = isChosen ? Color.black : Color.gray
    }
    var body : some View{
        let ringSize = isChosen ? geo.size.width * ballWidthPercent + 10 : geo.size.width * ballWidthPercent + 5
        ZStack{
            Circle().fill(strokeColor)
                .frame(width: ringSize,height: ringSize)
            Circle().fill(self.color.color)
                .frame(width: geo.size.width * ballWidthPercent, height: geo.size.width * ballWidthPercent)
        }
    }
}
