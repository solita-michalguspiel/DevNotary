//
//  ShareNoteSheet.swift
//  iosApp
//
//  Created by Michal Guspiel on 6.6.2022.
//

import SwiftUI
import shared

class NoteSharingData : ObservableObject{
    
    let viewModel = iosDI().getNotesDetailViewModel()
    var listener : Closeable? = nil
    
    @Published var sharingResponse : Any = ResponseEmpty.self
    
    func start(){
        listener = self.viewModel.watch(viewModel.noteSharingState,block : { response in
            print("New response \(response.debugDescription)")
            self.sharingResponse = response!
        })
    }
    func stop(){
        listener?.close()
    }
    
    @Published var email = ""{
        didSet{
            viewModel.anotherUserEmailAddress.setValue(String(email))
        }
    }
}
struct ShareNoteSheet : View{
    @Environment(\.dismiss) var dismiss
    @StateObject var noteSharingData = NoteSharingData()
    
    var body: some View{
        VStack{
            Text("Share note").font(.title)
            TextField("User e-mail address",text: $noteSharingData.email)
                .textInputAutocapitalization(.never)
                .disableAutocorrection(true)
                .padding(.horizontal, 30.0)
                .textFieldStyle(RoundedBorderTextFieldStyle())
            
            switch(noteSharingData.sharingResponse as Any){
            case _ as ResponseLoading:
                ProgressView()
            case _ as ResponseError:
                let error = noteSharingData.sharingResponse as! ResponseError
                Text(error.message)
                    .foregroundColor(.red)
                    .font(.caption)
            case _ as ResponseSuccess<AnyObject>:
                Text("Note shared successfully!").font(.caption)
            default:
                Text("")
            }
            
            CustomBorderedButton(text: "Share"){
                noteSharingData.viewModel.shareNote()
            }
        }.onAppear{noteSharingData.start()}
            .onDisappear{
                noteSharingData.stop()
                noteSharingData.viewModel.restartNoteSharingState()
            }
    }
}
