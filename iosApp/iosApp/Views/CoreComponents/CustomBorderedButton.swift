//
//  CustomBorderedButton.swift
//  iosApp
//
//  Created by Michal Guspiel on 1.6.2022.
//

import SwiftUI

struct CustomBorderedButton: View {
    
    let text : String
    let onClick : () -> ()
    
    init(text: String, onClick: @escaping () -> ()) {
        self.text = text
        self.onClick = onClick
    }
    
    var body: some View {
        Button(action : onClick){
            Text(text)
                .fontWeight(.semibold)
                .frame(minWidth : 50)
                .padding(.horizontal,15)
                .padding(.vertical,10)
                .background(Color.buttons)
                .foregroundColor(Color.white)
                .cornerRadius(10)
        }
    }
}

struct CustomBorderedButton_Previews: PreviewProvider {
    static var previews: some View {
        CustomBorderedButton(text : "TestButton"){
            print("Hello world!")
        }
    }
}
