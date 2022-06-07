//
//  SharedUsersListSheet.swift
//  iosApp
//
//  Created by Michal Guspiel on 7.6.2022.
//

import SwiftUI
import shared

class NoteSharedUsersData : ObservableObject{
    let viewModel = iosDI().getNotesDetailViewModel()
    var listeners : [Closeable] = []
    @Published var sharingResponse : Any = ResponseEmpty.self
    @Published var usersWithAccess : Array<User> = Array()
    
    func start(){
        viewModel.getUsersWithAccess()
        let noteSharingStateListener = self.viewModel.watch(viewModel.noteSharingState,block : { response in
            self.sharingResponse = response!
            print(self.sharingResponse)
        })
        
        let usersWithAccessListener =
        self.viewModel.watch(viewModel.usersWithAccess,block : {
            response in
            if(response is ResponseSuccess<AnyObject>){
                let responseWithUsers = response as! ResponseSuccess<AnyObject>
                self.usersWithAccess = responseWithUsers.data as! Array<User>
            }
            else{
                print("NOTE SHARED USERS DATA | Response different than success!!!")
            }
        })
        listeners.append(noteSharingStateListener)
        listeners.append(usersWithAccessListener)
    }
    func stop(){
        listeners.forEach{listener in
            listener.close()
        }
    }
}

struct SharedUsersListSheet: View {
    
    @Environment(\.dismiss) var dismiss
    @ObservedObject var noteSharedUsersData = NoteSharedUsersData()
    
    var body: some View {
        ScrollView{
            VStack{
                if(noteSharedUsersData.usersWithAccess.isEmpty){
                    Text("This note isn't shared with anyone").font(.title).padding(.vertical)
                }
                else{
                    Text("Users with access").font(.title).padding(.vertical)
                }
                Divider()
                ForEach(noteSharedUsersData.usersWithAccess,id : \.self){ user in
                    SharedUserRow(userEmailAddress: user.userEmail){
                        noteSharedUsersData.viewModel.unShareNote(sharedUserId: user.userId)
                    }
                    Divider()
                }
                
            }
        }.onAppear{
            noteSharedUsersData.start()
        }.onDisappear{
            noteSharedUsersData.stop()
        }
        
    }
}

struct SharedUsersListSheet_Previews: PreviewProvider {
    static var previews: some View {
        SharedUsersListSheet()
    }
}

struct SharedUserRow: View{
    let userEmailAddress: String
    let unShareNote : () -> ()
    var body: some View{
        HStack{
            Text(userEmailAddress)
                .padding(.leading,30)
            Spacer()
            Button(action: {
                unShareNote()
            }){
                Image(systemName: "trash.fill")
            }.padding(.trailing,30)
        }.padding(.vertical)
            .padding(.horizontal,10)
    }
}

struct SharedUserRowPreview : PreviewProvider{
    static var previews: some View{
        SharedUserRow(userEmailAddress: "test@gmail.com"){
            print("Test!")
        }
    }
}
