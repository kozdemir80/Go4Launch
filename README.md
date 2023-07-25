o4Lunch is a collaborative mobile application designed to help employees find and share restaurants for lunch. The app will have the following features:

Backend Integration: Go4Lunch will communicate with the Firebase backend to manage user accounts, authentication via Google and Facebook, data storage, and push notifications.

Login: Access to the app will be restricted, requiring users to log in with their Google or Facebook accounts.
![Go4_Sign_In](https://github.com/kozdemir80/Go4Launch/assets/84885403/f7f7d36d-8b40-4cff-92d1-4337f6afaf80)

Main Screens: The app will consist of three main views accessible via buttons at the bottom of the screen: Restaurant Map View, Restaurant List View, and Coworker View.
![14955527972631_Map Screen](https://github.com/kozdemir80/Go4Launch/assets/84885403/4e3b6640-aed4-446d-8369-a8b8ce6d3c48)

Restaurant Map View: After logging in, the user will be geolocated, and nearby restaurants will be displayed on the map with custom pins. The pin color will change to green if at least one coworker has chosen that restaurant. Users can click on a pin to see the restaurant details.

Restaurant List View: This view will show restaurant details, including name, distance, photo (if available), type, address, number of coworkers interested, opening hours, and reviews (0 to 3 stars).
![14955530288801_List Screen](https://github.com/kozdemir80/Go4Launch/assets/84885403/7b215ac9-45c3-4d84-b69b-1bd71063930f)

Restaurant Detail Page: Clicking on a restaurant will open a new screen showing additional details and options such as choosing the restaurant, calling, liking, visiting the website, and viewing the list of coworkers interested in that restaurant.
![14955545199687_Restaurant Detail Screen](https://github.com/kozdemir80/Go4Launch/assets/84885403/10fa2eef-a5e9-49b6-9807-929cdc81a096)

Coworker List: This screen displays all coworkers and their restaurant choices. Clicking on a coworker's entry will show the restaurant detail page.
![14955546022557_Co-Workers Screen](https://github.com/kozdemir80/Go4Launch/assets/84885403/5fc39490-0cf5-4743-8875-0f08e8ec720a)

Search Functionality: A magnifying glass icon will allow users to search for specific restaurants. The search is contextual and will update the data accordingly.
![14956345469069_Search Screens](https://github.com/kozdemir80/Go4Launch/assets/84885403/eafc5d6f-72e8-4a68-b95a-340ca7af26ab)

Menu: The top left of the screen will have a menu button showing the user's profile picture, first and last name, buttons to show the chosen restaurant, access settings, and sign-out option.
![Drawer_Menu](https://github.com/kozdemir80/Go4Launch/assets/84885403/6bdb5144-1d66-46d7-ab7b-39979d9e318e)

Notifications: Firebase will send a noon notification to users who have selected a restaurant, reminding them of their choice, the restaurant's address, and the list of coworkers going.
![Screenshot_1663275723](https://github.com/kozdemir80/Go4Launch/assets/84885403/12dfd589-7997-4dfe-bf51-122ebb2199b0)

Translations: The app will support both English and Spanish languages.

The Go4Lunch app aims to streamline the process of choosing lunch spots for coworkers, making it a collaborative and convenient experience for all users.
# Go4Launch
