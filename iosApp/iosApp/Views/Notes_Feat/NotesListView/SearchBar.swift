//
//  SearchBar.swift
//  iosApp
//
//  Created by Michal Guspiel on 7.6.2022.
//

import SwiftUI

struct SearchBarView: View {
    
    @Binding var text: String
    @State private var isEditing = false
    
    var body: some View {
        HStack {
            TextField("Search...", text: $text)
                .autocapitalization(.none)
                .disableAutocorrection(true)
                .foregroundColor(.black)
                .padding(7)
                .padding(.horizontal, 10)
                .padding(5)
                .background(Color.background)
                .cornerRadius(8)
                .overlay {
                    HStack {
                        Image(systemName: "magnifyingglass")
                            .foregroundColor(.gray)
                            .frame(minWidth: 0, maxWidth: .infinity,  alignment: .trailing)
                            .padding(.trailing, 8)
                    }
                }.onTapGesture {
                    isEditing = true
                }
            if isEditing {
                Button (action:{
                    text = ""
                    isEditing = false
                    UIApplication.shared.sendAction(#selector(UIResponder.resignFirstResponder), to: nil, from: nil, for: nil)
                }) {
                    Text("Cancel")
                }
                .padding(.trailing, 10)
                .transition(.move(edge: .trailing))
                .animation(.easeOut, value: 0.1)

            }
        }
    }
}
