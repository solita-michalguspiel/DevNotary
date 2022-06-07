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
    var listeners : [Closeable] = []
    
    @Published var sharingResponse : Any = ResponseEmpty.self
    
    func start(){
        let noteSharingStateListener = self.viewModel.watch(viewModel.noteSharingState,block : { response in
            self.sharingResponse = response!
            print(self.sharingResponse)
        })
        listeners.append(noteSharingStateListener)
    }
    func stop(){
        listeners.forEach{listener in
            listener.close()
        }
    }
    
    @Published var email = ""{
        didSet{
            viewModel.changeAnotherUserEmailAddress(newEmailAddress: String(email))
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
                Text("Note shared successfully!")
            default:
                Text("").onAppear{
                }
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
