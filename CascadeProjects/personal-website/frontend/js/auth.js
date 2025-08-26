document.addEventListener('DOMContentLoaded', () => {
    const loginBtn = document.getElementById('loginBtn');
    const registerBtn = document.getElementById('registerBtn');
    const loginModal = document.getElementById('loginModal');
    const registerModal = document.getElementById('registerModal');
    const closeLogin = document.getElementById('closeLogin');
    const closeRegister = document.getElementById('closeRegister');
    const switchToRegister = document.getElementById('switchToRegister');
    const switchToLogin = document.getElementById('switchToLogin');

    const loginForm = document.getElementById('loginForm');
    const registerForm = document.getElementById('registerForm');

    // Function to open a modal
    function openModal(modal) {
        modal.style.display = 'flex';
    }

    // Function to close a modal
    function closeModal(modal) {
        modal.style.display = 'none';
    }

    // Event Listeners for opening modals
    loginBtn.addEventListener('click', () => openModal(loginModal));
    registerBtn.addEventListener('click', () => openModal(registerModal));

    // Event Listeners for closing modals
    closeLogin.addEventListener('click', () => closeModal(loginModal));
    closeRegister.addEventListener('click', () => closeModal(registerModal));

    // Close modals when clicking outside
    window.addEventListener('click', (event) => {
        if (event.target == loginModal) {
            closeModal(loginModal);
        }
        if (event.target == registerModal) {
            closeModal(registerModal);
        }
    });

    // Switch between login and register modals
    switchToRegister.addEventListener('click', (event) => {
        event.preventDefault();
        closeModal(loginModal);
        openModal(registerModal);
    });

    switchToLogin.addEventListener('click', (event) => {
        event.preventDefault();
        closeModal(registerModal);
        openModal(loginModal);
    });

    // Handle Login Form Submission
    if (loginForm) {
        loginForm.addEventListener('submit', async (event) => {
            event.preventDefault();
            const usernameOrEmail = document.getElementById('loginUsername').value;
            const password = document.getElementById('loginPassword').value;

            try {
                console.log('Attempting login with:', { usernameOrEmail, password: '***' });
                const response = await apiService.loginUser({ usernameOrEmail, password });
                console.log('Login successful:', response);
                
                // Store user data in localStorage
                if (response.success && response.user) {
                    apiService.setCurrentUser(response.user);
                    localStorage.setItem('userName', response.user.firstName + ' ' + response.user.lastName);
                    localStorage.setItem('userRole', response.user.role);
                    localStorage.setItem('userId', response.user.id);
                }
                
                alert(`Welcome back, ${response.user.firstName}!`);
                closeModal(loginModal);
                updateUIForAuthenticatedUser(response.user);
                loadMovies(); // Load movies after successful login
            } catch (error) {
                console.error('Login failed:', error.message);
                alert(`Login failed: ${error.message}`);
            }
        });
    }

    // Handle Register Form Submission
    if (registerForm) {
        registerForm.addEventListener('submit', async (event) => {
            event.preventDefault();
            const firstName = document.getElementById('registerFirstName').value;
            const lastName = document.getElementById('registerLastName').value;
            const username = document.getElementById('registerUsername').value;
            const email = document.getElementById('registerEmail').value;
            const password = document.getElementById('registerPassword').value;
            const role = document.getElementById('registerRole').value;

            try {
                const response = await apiService.registerUser({ firstName, lastName, username, email, password, role });
                console.log('Registration successful:', response);
                
                // Store user data in localStorage after successful registration
                if (response.success && response.user) {
                    apiService.setCurrentUser(response.user);
                    localStorage.setItem('userName', response.user.firstName + ' ' + response.user.lastName);
                    localStorage.setItem('userRole', response.user.role);
                    localStorage.setItem('userId', response.user.id);
                }
                
                alert(`Welcome to CineFund, ${firstName}!`);
                closeModal(registerModal);
                updateUIForAuthenticatedUser(response.user);
                loadMovies(); // Load movies after successful registration
            } catch (error) {
                console.error('Registration failed:', error.message);
                alert(`Registration failed: ${error.message}`);
            }
        });
    }

    // Function to update UI for authenticated users
    function updateUIForAuthenticatedUser(user) {
        const loginBtn = document.getElementById('loginBtn');
        const registerBtn = document.getElementById('registerBtn');
        const navAuth = document.querySelector('.nav-auth');
        
        if (user && navAuth) {
            // Replace login/register buttons with user info and logout
            navAuth.innerHTML = `
                <span class="user-welcome">Welcome, ${user.firstName}!</span>
                <button class="btn btn-outline" id="logoutBtn">Logout</button>
            `;
            
            // Add logout functionality
            const logoutBtn = document.getElementById('logoutBtn');
            if (logoutBtn) {
                logoutBtn.addEventListener('click', logout);
            }
        }
    }

    // Function to logout user
    function logout() {
        apiService.logout();
        localStorage.removeItem('userName');
        localStorage.removeItem('userRole');
        localStorage.removeItem('userId');
        
        // Reset UI to unauthenticated state
        const navAuth = document.querySelector('.nav-auth');
        if (navAuth) {
            navAuth.innerHTML = `
                <button class="btn btn-outline" id="loginBtn">Login</button>
                <button class="btn btn-primary" id="registerBtn">Register</button>
            `;
            
            // Re-attach event listeners
            document.getElementById('loginBtn').addEventListener('click', () => openModal(loginModal));
            document.getElementById('registerBtn').addEventListener('click', () => openModal(registerModal));
        }
        
        alert('You have been logged out successfully!');
        // Clear movies or redirect to home
        const moviesGrid = document.getElementById('moviesGrid');
        if (moviesGrid) {
            moviesGrid.innerHTML = '<p>Please login to browse movies.</p>';
        }
    }

    // Check if user is already logged in on page load
    function checkAuthenticationStatus() {
        const currentUser = apiService.getCurrentUser();
        if (currentUser && apiService.isAuthenticated()) {
            updateUIForAuthenticatedUser(currentUser);
            loadMovies(); // Load movies if user is already authenticated
        }
    }

    // Call on page load
    checkAuthenticationStatus();
});
