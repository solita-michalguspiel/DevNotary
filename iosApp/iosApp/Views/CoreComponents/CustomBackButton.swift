//
//  CustomBackButton.swift
//  iosApp
//
//  Created by Michal Guspiel on 7.6.2022.
//

import SwiftUI

struct CustomBackButton: View {
    let appState : AppState
    var body: some View {
            Button(action:{self.appState.popToRootAndShowNotesList()}
            ){Image(systemName: "arrow.left")}
    }
}
