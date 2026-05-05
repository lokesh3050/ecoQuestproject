package com.ecoquest.app.data

data class Question(
    val text: String,
    val options: List<String>,
    val correctIndex: Int
)

object QuizData {

    private val cybersecurity = mapOf(
        "easy" to listOf(
            Question("What does CIA stand for?", listOf("Confidentiality, Integrity, Availability", "Control, Internet, Access", "Cyber, Information, Access", "Confidentiality, Internet, Authorization"), 0),
            Question("Which is a type of malware?", listOf("Firewall", "Antivirus", "Virus", "Router"), 2),
            Question("What is phishing?", listOf("Data backup", "Social engineering attack", "Encryption method", "Network tool"), 1),
            Question("Which device filters network traffic?", listOf("Router", "Switch", "Firewall", "Modem"), 2),
            Question("Which is a strong password?", listOf("123456", "password", "abc123", "T@9k!L2#"), 3),
            Question("HTTPS is used for:", listOf("Speed", "Secure communication", "Storage", "Gaming"), 1),
            Question("What is antivirus used for?", listOf("Increase speed", "Detect malware", "Store files", "Backup data"), 1),
            Question("What is a password?", listOf("Hardware", "Security key", "Network cable", "Virus"), 1),
            Question("Which is a cyber attack?", listOf("Printing", "Phishing", "Coding", "Browsing"), 1),
            Question("VPN is used for:", listOf("Gaming", "Secure connection", "Storage", "Editing"), 1)
        ),
        "moderate" to listOf(
            Question("Trojan malware is:", listOf("Self-replicating", "Disguised as legitimate software", "Only spyware", "Antivirus"), 1),
            Question("What does DoS mean?", listOf("Denial of Service", "Data over System", "Disk operating system", "Digital online security"), 0),
            Question("Which protocol is secure?", listOf("FTP", "HTTP", "HTTPS", "Telnet"), 2),
            Question("SQL Injection attacks:", listOf("CPU", "Database", "RAM", "Network cable"), 1),
            Question("XSS stands for:", listOf("Cross-Site Scripting", "Extra Secure System", "Cross Server Security", "Extended System Security"), 0),
            Question("Nmap is used for:", listOf("Scanning networks", "Password storage", "Encryption", "Backup"), 0),
            Question("MFA includes:", listOf("One password", "Password + OTP", "Username only", "Email only"), 1),
            Question("Brute force attack is:", listOf("Social engineering", "Guessing passwords repeatedly", "Encryption", "Backup"), 1),
            Question("AES is a:", listOf("Hashing algorithm", "Symmetric encryption", "Asymmetric encryption", "Firewall"), 1),
            Question("Zero-day vulnerability is:", listOf("Already fixed", "Unknown to vendor", "Not dangerous", "Deleted"), 1)
        ),
        "hard" to listOf(
            Question("Which attack intercepts communication between two parties?", listOf("DoS", "MITM", "Phishing", "Brute force"), 1),
            Question("Which tool is used for web vulnerability scanning?", listOf("Wireshark", "Nikto", "Excel", "Notepad"), 1),
            Question("Least privilege principle means:", listOf("Full access", "Minimum required access", "No access", "Admin access"), 1),
            Question("Hashing is used for:", listOf("Encryption", "Data integrity", "Networking", "Storage"), 1),
            Question("Which is an asymmetric algorithm?", listOf("AES", "DES", "RSA", "MD5"), 2),
            Question("CSRF attack tricks users into:", listOf("Installing software", "Performing unwanted actions", "Encrypting data", "Deleting files"), 1),
            Question("What does RBAC stand for?", listOf("Role-Based Access Control", "Rule-Based Access Control", "Risk-Based Access Control", "Remote Backup Access Control"), 0),
            Question("Packet sniffing is used to:", listOf("Block traffic", "Capture network packets", "Encrypt data", "Delete files"), 1),
            Question("Which layer does firewall mainly operate on?", listOf("Application layer", "Network layer", "Transport layer", "All layers (modern firewalls)"), 3),
            Question("What is the purpose of salting in hashing?", listOf("Increase speed", "Prevent rainbow table attacks", "Compress data", "Encrypt files"), 1)
        )
    )

    private val ai = mapOf(
        "easy" to listOf(
            Question("What does AI stand for?", listOf("Artificial Intelligence", "Automated Information", "Advanced Internet", "All In"), 0),
            Question("Which is a popular AI assistant?", listOf("Siri", "Photoshop", "Excel", "Chrome"), 0),
            Question("What does ML stand for?", listOf("Machine Learning", "Memory Logic", "Main Loop", "Motherboard Link"), 0),
            Question("Which company created ChatGPT?", listOf("Google", "OpenAI", "Microsoft", "Apple"), 1),
            Question("What is the Turing Test used for?", listOf("Testing internet speed", "Determining machine intelligence", "Fixing hardware", "Creating passwords"), 1),
            Question("Can AI create images from text?", listOf("No, only humans can", "Yes, using models like DALL-E", "Only via CSS", "Only in video games"), 1),
            Question("What is a bot?", listOf("A physical robot only", "A software application that runs automated tasks", "A type of virus", "A network router"), 1),
            Question("Which field relates to AI understanding human text?", listOf("Hardware Engineering", "Natural Language Processing", "Quantum Computing", "Database Management"), 1),
            Question("Is a calculator an AI?", listOf("Yes", "No, it just follows static rules", "Sometimes", "Only scientific calculators"), 1),
            Question("What is data in the context of ML?", listOf("Information used to train models", "Just text files", "Computer RAM", "Internet cables"), 0)
        ),
        "moderate" to listOf(
            Question("What is supervised learning?", listOf("Learning without labels", "Learning with labeled training data", "Learning through rewards", "Learning from dreaming"), 1),
            Question("What are Neural Networks inspired by?", listOf("Spider webs", "The human brain", "Computer circuits", "Traffic networks"), 1),
            Question("Which language is most associated with AI development?", listOf("HTML", "Python", "CSS", "PHP"), 1),
            Question("What is reinforcement learning?", listOf("Learning via labeled data", "Learning through trial, error, and rewards", "Grouping unlabelled data", "Database indexing"), 1),
            Question("What is computer vision?", listOf("Screens displaying images", "AI interpreting visual info from the world", "Wearing VR headsets", "Fixing blurry images"), 1),
            Question("What is a dataset?", listOf("A collection of data points", "A hard drive", "A cloud server", "A programming framework"), 0),
            Question("What does NLP stand for?", listOf("Neutral Language Program", "Natural Language Processing", "Node Level Protocols", "Network Link Provider"), 1),
            Question("What is an algorithm?", listOf("A computer bug", "A step-by-step set of instructions", "A piece of hardware", "A database table"), 1),
            Question("What is deep learning a subset of?", listOf("Web Development", "Machine Learning", "Quantum Computing", "Networking"), 1),
            Question("Which is a common library for ML in Python?", listOf("React", "TensorFlow", "Django", "Bootstrap"), 1)
        ),
        "hard" to listOf(
            Question("What is backpropagation?", listOf("Going back a web page", "An algorithm to calculate gradients in neural networks", "A type of virus", "Data backup"), 1),
            Question("What is the vanishing gradient problem?", listOf("When UI elements disappear", "When gradients become too small to update weights effectively", "When databases lose data", "When internet drops"), 1),
            Question("What is a Transformer model primarily dependent on?", listOf("Recurrence", "Convolutions", "Self-Attention mechanisms", "Q-learning"), 2),
            Question("What does GAN stand for?", listOf("Global Area Network", "Generative Adversarial Network", "Graphical AI Node", "General Algorithm Number"), 1),
            Question("What is overfitting?", listOf("Computer overheating", "Model learns training data too well but fails on new data", "Too much RAM used", "Loss of training data"), 1),
            Question("Which activation function outputs values between 0 and 1?", listOf("ReLU", "Sigmoid", "Tanh", "Leaky ReLU"), 1),
            Question("What are epochs in ML?", listOf("Server uptime", "One complete pass through the training dataset", "Number of layers", "Number of nodes"), 1),
            Question("What is hyperparameter tuning?", listOf("Optimizing the model's architectural/training settings", "Cleaning data", "Adding more RAM", "Writing more Python code"), 0),
            Question("What is transfer learning?", listOf("Copying files", "Using a pre-trained model as a starting point for a new task", "Moving from Python to C++", "Cloud migration"), 1),
            Question("In NLP, what is tokenization?", listOf("Creating passwords", "Breaking text into smaller pieces like words or subwords", "Encrypting databases", "Buying crypto"), 1)
        )
    )

    private val webdev = mapOf(
        "easy" to listOf(
            Question("What does HTML stand for?", listOf("Hyper Text Markup Language", "Home Tool Markup Language", "Hyperlinks Text Language", "Hyper Tool Markup Logic"), 0),
            Question("What is CSS used for?", listOf("Database management", "Styling web pages", "Server logic", "Operating systems"), 1),
            Question("Which HTML tag is used for the largest heading?", listOf("<h6>", "<header>", "<h1>", "<head>"), 2),
            Question("Which tag is used to create a hyperlink?", listOf("<a>", "<link>", "<href>", "<url>"), 0),
            Question("What does JS stand for?", listOf("JavaStyle", "JavaScript", "JustScript", "JelloScript"), 1),
            Question("Which CSS property changes text color?", listOf("text-color", "font-color", "color", "textColor"), 2),
            Question("What is the correct tag for inserting an image?", listOf("<image>", "<img>", "<pic>", "<src>"), 1),
            Question("Is HTML a programming language?", listOf("Yes", "No, it's a markup language", "Only in HTML5", "Yes, for backend"), 1),
            Question("Who makes Web standards?", listOf("Google", "Microsoft", "The World Wide Web Consortium (W3C)", "Mozilla"), 2),
            Question("Which CSS property controls text size?", listOf("font-style", "text-size", "font-size", "text-style"), 2)
        ),
        "moderate" to listOf(
            Question("What is the DOM?", listOf("Document Object Model", "Data Oriented Module", "Digital Object Maker", "Desktop Operation Mode"), 0),
            Question("In JS, what does `let` do?", listOf("Declares a constant", "Declares a block-scoped variable", "Creates a function", "Loops through arrays"), 1),
            Question("Which array method adds an element to the end?", listOf("push()", "pop()", "shift()", "unshift()"), 0),
            Question("What is responsive web design?", listOf("Sites that load fast", "Sites that adapt to different screen sizes", "Sites with working forms", "Sites with JavaScript"), 1),
            Question("What does API stand for?", listOf("Application Programming Interface", "Advanced Program Integration", "Apple Public Internet", "Automated Process Interaction"), 0),
            Question("What is a CSS pseudo-class?", listOf("A fake class", "A state of an element (e.g., :hover)", "An inline style", "A JavaScript function"), 1),
            Question("What is the output of `typeof null` in JS?", listOf("null", "undefined", "object", "string"), 2),
            Question("What is Flexbox used for?", listOf("Drawing graphics", "One-dimensional layout modeling", "Database queries", "Server hosting"), 1),
            Question("Which HTTP method is used to update data?", listOf("GET", "POST", "PUT/PATCH", "DELETE"), 2),
            Question("What does JSON stand for?", listOf("Java Syntax Object Network", "JavaScript Object Notation", "Java Standard Output Node", "JavaScript Output Name"), 1)
        ),
        "hard" to listOf(
            Question("What is a closure in JavaScript?", listOf("Closing a browser tab", "A function bundled with its lexical environment", "An IF statement", "A CSS selector"), 1),
            Question("What does CSS Specificity resolve?", listOf("Which rule is applied when multiple rules target the same element", "How fast CSS loads", "Which font is used", "HTML validation errors"), 0),
            Question("Describe Event Delegation:", listOf("Attaching a single listener to a parent element to manage child events", "Creating custom events", "Delegating server tasks", "Stopping event propagation"), 0),
            Question("What is the Virtual DOM?", listOf("A lightweight JS representation of the real DOM used by React", "A VR headset interface", "A type of database", "A CSS framework"), 0),
            Question("What is CORS?", listOf("Cross-Origin Resource Sharing", "Central Object Rendering System", "CSS Object Request Syntax", "Cascading Origin Rules System"), 0),
            Question("What is Server-Side Rendering (SSR)?", listOf("Rendering components to HTML strings on the server", "Rendering only on the client browser", "Storing data in SQL", "Using CSS Grid"), 0),
            Question("What does the `this` keyword refer to in arrow functions?", listOf("The global window object", "It inherits `this` from the parent scope entirely", "The event target", "The new object instances"), 1),
            Question("What is a Web Worker?", listOf("A developer", "A JS script executed in the background, independent of UI scripts", "A CSS animation", "An HTML form element"), 1),
            Question("In CSS Grid, what does `1fr` mean?", listOf("1 pixel", "1 fraction of the available space", "1 frame per second", "1 font unit"), 1),
            Question("What is the purpose of JWT?", listOf("Styling text", "Securely transmitting information between parties as a JSON object", "Querying a database", "Looping animations"), 1)
        )
    )

    private val cloud = mapOf(
        "easy" to listOf(
            Question("What is Cloud Computing?", listOf("Weather forecasting software", "Delivering computing services over the internet", "A physical hard drive", "A type of WiFi"), 1),
            Question("Which is a major Cloud provider?", listOf("Amazon Web Services (AWS)", "Nintendo", "Samsung", "Ford"), 0),
            Question("What does SaaS stand for?", listOf("System as a Server", "Software as a Service", "Storage and Security", "Standard Architecture Service"), 1),
            Question("Which is an example of Cloud Storage?", listOf("A USB drive", "Google Drive", "A DVD", "Floppy Disk"), 1),
            Question("What does scaling mean in cloud?", listOf("Weighing servers", "Adjusting resources to meet demand", "Cleaning hard drives", "Updating passwords"), 1),
            Question("What is a data center?", listOf("A spreadsheet", "A physical facility housing computer systems", "A math formula", "A social network"), 1),
            Question("Is cloud computing typically pay-as-you-go?", listOf("Always fixed price", "Yes, usually pay-as-you-go", "It's always free", "Only for enterprise"), 1),
            Question("Which of these is a PaaS?", listOf("Heroku", "Microsoft Word", "A monitor", "A mouse"), 0),
            Question("What is required to access the cloud?", listOf("A VPN", "Internet connection", "A specialized computer", "A server room"), 1),
            Question("Which Google service corresponds to Cloud storage?", listOf("Gmail", "Google Cloud Storage", "Google Maps", "Google Docs"), 1)
        ),
        "moderate" to listOf(
            Question("What is the difference between IaaS and PaaS?", listOf("PaaS abstracts OS/runtime; IaaS gives raw VM access", "IaaS is for databases; PaaS is for storage", "No difference", "IaaS is free, PaaS is paid"), 0),
            Question("What is horizontal scaling?", listOf("Adding more power (CPU/RAM) to a single server", "Adding more servers/instances to share the load", "Buying a wider monitor", "Upgrading the OS"), 1),
            Question("What is an AWS EC2 instance?", listOf("A database", "A Virtual Storage block", "A Virtual Private Server (VM)", "A CDN"), 2),
            Question("What is a VPC?", listOf("Virtual Personal Computer", "Virtual Private Cloud", "Video Processing Component", "Verified Public Cloud"), 1),
            Question("What is serverless computing?", listOf("Using no computers at all", "Provider manages server allocation dynamically; you just deploy code", "Hosting at home", "Free cloud computing"), 1),
            Question("What is Docker used for?", listOf("Creating network cables", "Containerization of applications", "Designing UI", "Database architecture"), 1),
            Question("What does CDN stand for?", listOf("Content Delivery Network", "Cloud Data Node", "Centralized Data Network", "Client Download Network"), 0),
            Question("What does S3 stand for in AWS?", listOf("Simple Storage Service", "Super Secure System", "Server Solution Stack", "Standard System Storage"), 0),
            Question("What is a load balancer?", listOf("A physical weight", "A device acting as a reverse proxy to distribute network traffic", "A storage unit", "A database query"), 1),
            Question("What is an SLA?", listOf("Server Layer Architecture", "Service Level Agreement", "Storage Language Application", "Secure Login Authentication"), 1)
        ),
        "hard" to listOf(
            Question("What is Kubernetes primarily used for?", listOf("Writing HTML", "Container orchestration", "Video editing", "SQL queries"), 1),
            Question("What is the CAP theorem?", listOf("Consistency, Availability, Partition Tolerance", "Cloud, Application, Platform", "Compute, API, Performance", "Cost, Architecture, Predictability"), 0),
            Question("What is a serverless 'cold start'?", listOf("A server in a cold room", "The latency incurred when a serverless function is invoked after being idle", "A frozen database", "Turning a laptop on"), 1),
            Question("What differentiates a Multi-Cloud strategy from Hybrid-Cloud?", listOf("Multi uses multiple public providers; Hybrid mixes public with private/on-prem", "They are exactly the same", "Hybrid is strictly AWS+Azure", "Multi-cloud is only for storage"), 0),
            Question("What is Infrastructure as Code (IaC)?", listOf("Writing rules via firewall", "Managing and provisioning infra through machine-readable definition files", "Writing CSS", "A programming language"), 1),
            Question("Which tool is standard for IaC?", listOf("Photoshop", "Terraform", "Wordpress", "JQuery"), 1),
            Question("What is a microservices architecture?", listOf("One giant codebase", "Structuring an application as a collection of loosely coupled services", "Using tiny hardware", "Using only mobile apps"), 1),
            Question("What is an API Gateway?", listOf("A firewall", "A management tool that sits between a client and a collection of back-end services", "A database", "A domain registrar"), 1),
            Question("What is blue-green deployment?", listOf("Coloring the UI", "A release strategy creating two identical environments to reduce downtime", "A type of server rack", "A database backup"), 1),
            Question("What is edge computing?", listOf("Dropping servers off a cliff", "Performing computation geographically closer to the data source", "Using old hardware", "Relying purely on central data centers"), 1)
        )
    )

    private val allQuizzes: Map<String, Map<String, List<Question>>> = mapOf(
        "cybersecurity" to cybersecurity,
        "ai" to ai,
        "webdev" to webdev,
        "cloud" to cloud
    )

    fun getQuestions(topic: String, difficulty: String): List<Question> {
        return allQuizzes[topic]?.get(difficulty) ?: cybersecurity["easy"]!!
    }

    fun getTopics(): List<Pair<String, String>> = listOf(
        "cybersecurity" to "Cybersecurity",
        "ai" to "Artificial Intelligence",
        "webdev" to "Web Development",
        "cloud" to "Cloud Computing"
    )
}
