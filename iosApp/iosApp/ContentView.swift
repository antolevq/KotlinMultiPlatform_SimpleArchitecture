import SwiftUI
import shared

struct ContentView: View {
  @ObservedObject private(set) var viewModel: ViewModel
    
    

    var body: some View {
        NavigationView {
            listView()
            .navigationBarTitle("SpaceX Launches")
            .navigationBarItems(trailing:
                Button("Reload") {
                    self.viewModel.loadLaunches( forceReload: true)
            })
        }
    }

    private func listView() -> AnyView {
        switch viewModel.launches {
        case .loading:
            return AnyView(Text("Loading...").multilineTextAlignment(.center))
        case .result(let launches):
            return AnyView(List(launches) { launch in
                RocketLaunchRow(rocketLaunch: launch)
            })
        case .error(let description):
            return AnyView(Text(description).multilineTextAlignment(.center))
        }
    }
}

extension ContentView {

    enum LoadableLaunches {
        case loading
        case result([RocketLaunch])
        case error(String)
    }

    class ViewModel: ObservableObject {
        @Published var launches = LoadableLaunches.loading
        
        let usecase = GetRocketListUseCase(rRepository: RocketRespository(databaseDriverFactory: DatabaseDriverFactory()), forceReload: true)

        init() {
            self.loadLaunches(forceReload: false)
        }

        func loadLaunches(forceReload: Bool) {
            self.launches = .loading
            usecase.setForceReload(v: forceReload)
            usecase.execute { (result, error) in
                if (result?.status == Status.successful) {
                    self.launches = .result((result?.data)!)
                } else {
                    self.launches = .error(error?.localizedDescription ?? "error")
                }
                
            }
            
//            when (res) {
//                is Result.Success -> {
//                    progressBarView.isVisible = false
//                    launchesRvAdapter.launchEntities = res.data
//                    launchesRvAdapter.notifyDataSetChanged()
//                }
//                is Result.Error -> {
//                    progressBarView.isVisible = false
//                    Toast.makeText(this@MainActivity, res.message, Toast.LENGTH_SHORT).show()
//                }
//                else -> {
//                    progressBarView.isVisible = true
//                }
//            }
            
            
//            sdk.getLaunches(forceReload: forceReload, completionHandler: { launches, error in
//                if let launches = launches {
//                    self.launches = .result(launches)
//                } else {
//                    self.launches = .error(error?.localizedDescription ?? "error")
//                }
//            })
        }
    }
}

extension RocketLaunch: Identifiable { }
