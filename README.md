
===

# Foodies

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)

## Overview
#### Foodies helps users meet fellow food-lovers along their gourmet journeys.


### App Evaluation
- **Category:** Social Networking / Food
- **Mobile:** This app would be primarily developed for mobile but would perhaps be just as viable on a computer, such as Yelp or other similar apps. Functionality wouldn't be limited to mobile devices, however mobile version could potentially have more features.
- **Story:** Lists out local restaurants and allows users to form groups to eat out together.
- **Market:** Any individual could choose to use this app. For instanct, we are expecting Foodies to be widely used among college freshmen, when they first arrive at the campus and actively meeting new people.
- **Habit:** This app could be used as often or unoften as the user wanted depending on how frequent they go out for food or want to meet new people.
- **Scope:** First we would start with displaying restraunt information and letting users to form a group. Then perhaps add communication (e.g. group chat) and payment methods within the app. Large potential to work with local restaurants, giving them an advertisement platform.

## Product Spec
### 1. User Stories 

**Required Must-have Stories**

- [X] User logs in to access groups and previous chats.
- [X] User creates profile when signing up for the first time. Necessary information includes username, password, interest tag (in general), short introduction, avatar.
- [X] User sees a list of popular restaurants with basic information around his/her location.
- [X] User picks a restaurant to view the restaurant details page, including two options: Create New Group or Join an Exisiting Group. 
- [X] The user who creates the group is the leader, and is responsible for setting up the time to meet, # of members. He/She also has the authority to accpet/deny new member application.
- [X] User can check the profile page of each existing group member in a group. 
- [X] User joins the group chat with other group members upon joining a group, and he/she is automatically excluded from the group chat when he/she decides to quit the group.

**Optional Nice-to-have Stories**

- [X] Search restaurant with a yelp search engine.
- [X] Display surrounding restaurants in Map view.
- [X] Direction to the restaurant (e.g. link to Google Map).
- [X] User can sign in with other accounts (e.g. Google account).
- [ ] User can decided to share their past visits to restaurants (good credit!).
- [ ] Group members can rate each other after the set meeting/dining time (good credit!).
- [ ] Group displays extra information such as a short introduction or interest tags ("Movie", "Games", "Music")
- [ ] Group leader enters the total bill amount and splits the bill between group members.
- [ ] User views a list of saved restaurants.


### 2. Screen Archetypes

* Login 
    * Automatic login when user closes and reopens the app after previous successful login  
* Register - User signs up or logs into their account 
    * Upon Download/Reopening of the application, the user is prompted to log in to gain access to their profile information to be properly matched with another person. When signing up, user needs to choose a username, password, profile photo and short description.
* Restaurant List
    * Include location, type of cuisine, ratings, # of reviews, price range. 
* Restaurant Detail 
    * Include location, short description, type of cuisine, ratings, price range, # of reviews (and actual reviews) . 
* Messaging Screen - Chat for users to communicate in groups
    * Upon joining a group, automatically join users to the group chat
    * View previous group chats
* Profile Screen 
    * View other users' profile 
    * Allows user to edit profile information when viewing their own profile
* Settings Screen (optional)
    * 

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* List of restaurants
* Profile
* Settings (optional)

Optional:
* Music/Encounter Queue
* Discover (Top Choices)

**Flow Navigation** (Screen to Screen)
* Forced Log-in -> Account creation if no log in is available
* Music Selection (Or Queue if Optional) -> Jumps to Chat
* Profile -> Text field to be modified. 
* Settings -> Toggle settings

## Wireframe

<img src="https://i.imgur.com/pWtfaJL.png" title="Wireframe for profile, groupchat, and group info" width='650' alt="wireframe1">
<img src="https://i.imgur.com/X6cBNGi.png" title="Overall screen flow" width='900' alt="screen flow">


## Schema
### Models
#### User

| Property      | Type     | Description |
   | ------------- | -------- | ------------|
| objectId      | String   | unique id for the user (default field) |
| createdAt     | Date | date when user account is created (default field) |
| updatedAt     | Date | date when user account is last updated (default field) |
| profile       | File | user's profile photo |
| Location      | GeoPoint | user's current location (device location) |
| username      | String | displayed username in app |
| password      | String | user's password |
| email         | String | user's email. entered at sign up process |
| description   | String | self-introduction |
| groupList     | Array | list of groups the user has joined so far |

#### Group

| Property      | Type     | Description |
   | ------------- | -------- | ------------|
| objectId      | String   | unique id for the group (default field) |
| createdAt     | Date | date when group is created (default field) |
| updatedAt     | Date | date when group information is last updated (default field) |
| restaurantName | String | the name of the restaurant group plans to meet at |
| restaurantAddress | String | address of the restaurant |
| restaurantID   | String | restaurant ID from Yelp API |
| FounderID     | Pointer to User | pointer to the user who created group |
| description   | String | group description (e.g. meeting purpose/goal) |
| name          | String | display name of the group |
| Time          | String | the time group will meet at restaurant |
| Date          | String | the date group will meet at restaurant |
| full          | Boolean | has the group reached max member number? |
| memberList    | Array  | array of member IDs (user ID) |
| curMember     | Number | current number of members in the group
| maxMember     | Number | maximum number of members allowed to join the group |


#### Message

| Property      | Type     | Description |
   | ------------- | -------- | ------------|
| objectId      | String   | unique id for the message (default field) |
| createdAt     | Date | date when message is sent (default field) |
| updatedAt     | Date | date when message is last updated (default field) |
| userID        | String | ID of the user who sent the message |
| body          | String | message content |
| groupID       | String | ID of the group this message was sent in |


## Networking
### Yelp API endpoints
<img src="https://i.imgur.com/hc1vBEK.png" title="Yelp API list" width='900' alt="screen flow">

#### List of network requests by screen
- Discover Feed Screen
  - (Read/GET) Query nearby restaurants from
- Restaurant Details Screen
  - (Read/GET) Query restaurant details information from Yelp
  - (Read/GET) Query list of groups under this restaurants name
- Group Details Screen
  - (Read/GET) Read the group founder information as well as group member information
- Message Screen
  - (Read/GET) Query the groups current user has joined
- Chatroom Screen
  - (Read/Get) Query the messages sent to this chatroom
  - (Create/Post) Create a new message in the chatroom
- Profile Screen
  - (Read/GET) Query logged in user object
  - (Update/PUT) Update user profile image

## Video Walkthrough

Here's a walkthrough of implemented user stories:

### Login 
- Via Facebook  
- <img src='https://i.imgur.com/4dSmVfv.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />
- Via email registration. User should use an active email to validate their Foodies account.  
- <img src='https://i.imgur.com/YkVDrAW.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

### Discover page: Discover and Search for Restraurants
- Discover page. Restaurants are listed based on Yelp's recommendation order.  
- <img src='https://i.imgur.com/DAg7b1T.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />
- Search example (keyward: french, burger)  
- <img src='https://i.imgur.com/i35ieN4.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

### View restaurant detail by seleting a card on Discover page
- Restaurant details  
- <img src='https://i.imgur.com/aP9S1cm.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' /> 
For each restaurant, users can either choose to join a existing restaurant, or create their own.


### Create a group!
- To create a new group, users must provide group name, description, maximum number of members, meeting time, and meeting location.  
- <img src='https://i.imgur.com/6XTlJn3.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

### Messages tab: View joined groups
- Messages tab includes shows all groups group chats the user has joined before. When they join a group, they are automatically invited to the group chat.  
- <img src='https://i.imgur.com/ljdyOGd.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />



### Chatroom: Live group chat
- Group chats are automatically updated so that each users receive messages instantly.
- <img src='https://i.imgur.com/qucsqLa.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />
- <img src='https://i.imgur.com/5FkM63l.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

### Profile tab: Make a post and Edit user profile
- On Foodies, users can post images and captions (like a mini-instagram)!
- <img src='https://i.imgur.com/E43nkk1.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

- Users can change their profile image and bio (short description/introduction).
- Users who joined via email, they can update user ID and password.


### Maps: Restaurants, Groups, Members
- Depending on where the user enters the map view (top right icon), they can view one of Restaurants, Groups, or Members' map view.
- Discover page --> Location of restaurants that are listed on the discover page
- <img src='https://i.imgur.com/dQgwmcK.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />
- Messages page --> Location of restaurants associated with each group.
- <img src='https://i.imgur.com/i9XQoA4.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />
- Group chat room --> Location of members of that group.
- <img src='https://i.imgur.com/X6v6Z68.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

<!-- Log in to Discover:  

<img src='https://i.imgur.com/bmS2t81.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

App Icon, Splash Screen and Start Activity:

<img src='https://i.imgur.com/quy0WxC.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

Sign Up Page:

<img src='https://i.imgur.com/HMyENYy.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

Log In Page:

<img src='https://i.imgur.com/KkrIMeO.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

Main Screen with Bottom Navigation View and ToolBar. Discover nearby Restaurant Page with use of Phone's GPS location and infinite Scrolling, use retrofit to get Yelp API:

<img src='https://i.imgur.com/954LDTh.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

Search restaurants by keywords and can cancel to return to Normal searches:

<img src='https://i.imgur.com/3d1CV13.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

Display detail view of restaurant, also the page where user can form or join a group:

<img src='https://i.imgur.com/cRxun8x.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

Start a new group registration page:

<img src='https://i.imgur.com/RcT43Gf.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

Group Registration and back to display List of Groups:

<img src='https://i.imgur.com/uFHnB7Y.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' /> -->
