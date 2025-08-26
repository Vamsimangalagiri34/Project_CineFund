// Main JavaScript functionality
document.addEventListener('DOMContentLoaded', () => {
    // Mobile navigation toggle
    const hamburger = document.querySelector('.hamburger');
    const navMenu = document.querySelector('.nav-menu');

    if (hamburger && navMenu) {
        hamburger.addEventListener('click', () => {
            hamburger.classList.toggle('active');
            navMenu.classList.toggle('active');
        });

        // Close mobile menu when clicking on nav links
        document.querySelectorAll('.nav-link').forEach(link => {
            link.addEventListener('click', () => {
                hamburger.classList.remove('active');
                navMenu.classList.remove('active');
            });
        });
    }

    // Smooth scrolling for navigation links
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });

    // Hero buttons functionality
    const startInvestingBtn = document.getElementById('startInvestingBtn');
    const learnMoreBtn = document.getElementById('learnMoreBtn');

    if (startInvestingBtn) {
        startInvestingBtn.addEventListener('click', () => {
            // Check if user is authenticated
            if (apiService.isAuthenticated()) {
                // Scroll to movies section
                document.getElementById('movies').scrollIntoView({
                    behavior: 'smooth'
                });
            } else {
                // Open register modal
                document.getElementById('registerModal').style.display = 'flex';
            }
        });
    }

    if (learnMoreBtn) {
        learnMoreBtn.addEventListener('click', () => {
            document.getElementById('about').scrollIntoView({
                behavior: 'smooth'
            });
        });
    }

    // Contact form functionality
    const contactForm = document.getElementById('contactForm');
    if (contactForm) {
        contactForm.addEventListener('submit', (e) => {
            e.preventDefault();
            
            const name = document.getElementById('contactName').value;
            const email = document.getElementById('contactEmail').value;
            const message = document.getElementById('contactMessage').value;

            // Simple form validation
            if (!name || !email || !message) {
                alert('Please fill in all fields.');
                return;
            }

            // Here you would typically send the form data to your backend
            alert('Thank you for your message! We will get back to you soon.');
            contactForm.reset();
        });
    }

    // Load statistics (mock data for now)
    loadStatistics();

    // Navbar scroll effect
    window.addEventListener('scroll', () => {
        const navbar = document.querySelector('.navbar');
        if (window.scrollY > 50) {
            navbar.classList.add('scrolled');
        } else {
            navbar.classList.remove('scrolled');
        }
    });
});

// Function to load and display statistics
async function loadStatistics() {
    try {
        // Mock statistics - in real app, these would come from API
        const stats = {
            totalInvestments: 2500000,
            totalMovies: 45,
            totalInvestors: 1200,
            totalReturns: 850000
        };

        // Animate numbers
        animateNumber('totalInvestments', stats.totalInvestments, '$');
        animateNumber('totalMovies', stats.totalMovies);
        animateNumber('totalInvestors', stats.totalInvestors);
        animateNumber('totalReturns', stats.totalReturns, '$');

    } catch (error) {
        console.error('Failed to load statistics:', error);
    }
}

// Function to animate numbers
function animateNumber(elementId, targetValue, prefix = '') {
    const element = document.getElementById(elementId);
    if (!element) return;

    const duration = 2000; // 2 seconds
    const startTime = performance.now();
    const startValue = 0;

    function updateNumber(currentTime) {
        const elapsed = currentTime - startTime;
        const progress = Math.min(elapsed / duration, 1);
        
        // Easing function for smooth animation
        const easeOutQuart = 1 - Math.pow(1 - progress, 4);
        const currentValue = Math.floor(startValue + (targetValue - startValue) * easeOutQuart);
        
        element.textContent = prefix + formatNumber(currentValue);
        
        if (progress < 1) {
            requestAnimationFrame(updateNumber);
        }
    }
    
    requestAnimationFrame(updateNumber);
}

// Function to format numbers with commas
function formatNumber(num) {
    return new Intl.NumberFormat().format(num);
}

// Intersection Observer for animations
const observerOptions = {
    threshold: 0.1,
    rootMargin: '0px 0px -50px 0px'
};

const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
        if (entry.isIntersecting) {
            entry.target.classList.add('animate');
        }
    });
}, observerOptions);

// Observe elements for animation
document.addEventListener('DOMContentLoaded', () => {
    const animateElements = document.querySelectorAll('.feature-card, .stat-card, .about-card');
    animateElements.forEach(el => observer.observe(el));
});
