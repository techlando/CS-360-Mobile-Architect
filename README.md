# CS-360-Mobile-Architect

## Briefly summarize the requirements and goals of the app you developed. What user needs was this app designed to address?
The goal of the app was to help users track their weight over time and receive a text message once their goal weight was reached. It was designed for people on a fitness or health improvement journey who wanted a simple, no-frills way to log progress, set personal goals, and get alerted when they achieved them. The main focus was functionality and ease of use.

## What screens and features were necessary to support user needs and produce a user-centered UI for the app? How did your UI designs keep users in mind? Why were your designs successful?
The app includes a login screen, a dashboard with the weight log, a form to add or update weight entries, and a separate notifications screen to toggle SMS alerts. Everything was built with simplicity in mind—clear labels, minimal navigation, and intentional layout choices. I made sure users could quickly enter data and see progress without digging through menus. Giving users control over notifications made the app feel more personal and respectful of their preferences.

## How did you approach the process of coding your app? What techniques or strategies did you use? How could those techniques or strategies be applied in the future?
I broke things down into small, manageable tasks—focusing on one feature at a time and testing as I went. I used a lot of condition checks to handle edge cases, especially around SMS permissions and user input. Keeping the logic modular and reusing components helped keep the code organized. That incremental approach really helped me stay on track, and I’ll definitely use it again in future projects.

## How did you test to ensure your code was functional? Why is this process important, and what did it reveal?
I tested everything on an Android emulator, verifying the full user flow from login to weight entry to goal alerts. I also tested different permission states—like what happens when users deny SMS access—to make sure the app handles those cases gracefully. This helped catch issues with state management early and made the app more stable overall.

## Consider the full app design and development process from initial planning to finalization. Where did you have to innovate to overcome a challenge?
One of the trickiest parts was the SMS functionality. Since you can’t send real texts on the emulator, I had to simulate the logic and ensure the code would behave correctly when run on a real device. I also had to rethink how and when to request permissions so it wouldn’t disrupt the user experience. It took some trial and error, but I was able to make it work with a shared preference toggle and conditionally triggered alerts.

## In what specific component of your mobile app were you particularly successful in demonstrating your knowledge, skills, and experience?
The goal weight alert feature is where everything came together, database interaction, conditional logic, permission handling, and UI feedback. It’s simple on the surface, but it tied a lot of different parts of the app together. I’m proud of how that part functions and how it balances user control with automated feedback. It shows I understand how to design around both user experience and system limitations.
