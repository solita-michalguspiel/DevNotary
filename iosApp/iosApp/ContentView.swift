//
//  ContentView.swift
//  iosApp
//
//  Created by Michal Guspiel on 20.5.2022.
//

import SwiftUI
import shared
var authViewModel = iosDI().getAuthViewModel()
var sameViewModel = iosDI().getAuthViewModel()
struct ContentView: View {
    
    var string = String("authViewModel.timer.seconds")
    
    var body: some View {
        VStack{
            Spacer()
        Text(string)
            .padding()
        }

    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
