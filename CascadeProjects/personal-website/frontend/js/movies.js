// Movies functionality
document.addEventListener('DOMContentLoaded', () => {
    const moviesGrid = document.getElementById('moviesGrid');
    const moviesLoading = document.getElementById('moviesLoading');
    const filterButtons = document.querySelectorAll('.filter-btn');
    
    let allMovies = [];
    let currentFilter = 'all';

    // Function to load movies from API
    async function loadMovies() {
        // Check if user is authenticated
        if (!apiService.isAuthenticated()) {
            showMoviesMessage('Please login to browse movies.');
            return;
        }

        try {
            showLoading(true);
            const response = await apiService.getAllMovies();
            
            if (response.success && response.data) {
                allMovies = response.data;
                displayMovies(allMovies);
            } else {
                showMoviesMessage('No movies available at the moment.');
            }
        } catch (error) {
            console.error('Failed to load movies:', error);
            showMoviesMessage('Failed to load movies. Please try again later.');
        } finally {
            showLoading(false);
        }
    }

    // Function to display movies in the grid
    function displayMovies(movies) {
        if (!movies || movies.length === 0) {
            showMoviesMessage('No movies found for the selected filter.');
            return;
        }

        const moviesHTML = movies.map(movie => createMovieCard(movie)).join('');
        moviesGrid.innerHTML = moviesHTML;

        // Add click listeners to movie cards
        document.querySelectorAll('.movie-card').forEach(card => {
            card.addEventListener('click', () => {
                const movieId = card.dataset.movieId;
                showMovieDetails(movieId);
            });
        });
    }

    // Function to create a movie card HTML
    function createMovieCard(movie) {
        const fundingProgress = movie.budget > 0 ? 
            ((movie.raisedAmount / movie.budget) * 100).toFixed(1) : 0;
        
        const statusClass = movie.status.toLowerCase();
        const statusText = movie.status.replace('_', ' ');

        return `
            <div class="movie-card" data-movie-id="${movie.id}">
                <div class="movie-poster">
                    <img src="${movie.posterUrl || 'https://via.placeholder.com/300x450?text=No+Poster'}" 
                         alt="${movie.title}" onerror="this.src='https://via.placeholder.com/300x450?text=No+Poster'">
                    <div class="movie-status ${statusClass}">${statusText}</div>
                </div>
                <div class="movie-info">
                    <h3 class="movie-title">${movie.title}</h3>
                    <p class="movie-producer">by ${movie.producerName || 'Unknown Producer'}</p>
                    <p class="movie-genre">${movie.genre || 'Drama'}</p>
                    <div class="movie-funding">
                        <div class="funding-progress">
                            <div class="progress-bar">
                                <div class="progress-fill" style="width: ${fundingProgress}%"></div>
                            </div>
                            <span class="progress-text">${fundingProgress}% funded</span>
                        </div>
                        <div class="funding-amounts">
                            <span class="raised">$${formatNumber(movie.raisedAmount || 0)}</span>
                            <span class="target">/ $${formatNumber(movie.budget)}</span>
                        </div>
                    </div>
                    ${movie.expectedReturnPercentage ? 
                        `<div class="expected-return">Expected Return: ${movie.expectedReturnPercentage}%</div>` : ''}
                </div>
            </div>
        `;
    }

    // Function to show movie details modal
    async function showMovieDetails(movieId) {
        try {
            const response = await apiService.getMovieById(movieId);
            if (response.success && response.data) {
                displayMovieModal(response.data);
            }
        } catch (error) {
            console.error('Failed to load movie details:', error);
            alert('Failed to load movie details.');
        }
    }

    // Function to display movie details in modal
    function displayMovieModal(movie) {
        const modal = document.getElementById('movieModal');
        const movieTitle = document.getElementById('movieTitle');
        const movieDetails = document.getElementById('movieDetails');
        
        movieTitle.textContent = movie.title;
        
        const fundingProgress = movie.budget > 0 ? 
            ((movie.raisedAmount / movie.budget) * 100).toFixed(1) : 0;

        movieDetails.innerHTML = `
            <div class="movie-detail-content">
                <div class="movie-detail-left">
                    <img src="${movie.posterUrl || 'https://via.placeholder.com/400x600?text=No+Poster'}" 
                         alt="${movie.title}" class="movie-detail-poster">
                </div>
                <div class="movie-detail-right">
                    <div class="movie-meta">
                        <span class="movie-status ${movie.status.toLowerCase()}">${movie.status.replace('_', ' ')}</span>
                        <span class="movie-genre">${movie.genre || 'Drama'}</span>
                    </div>
                    <p class="movie-description">${movie.description || 'No description available.'}</p>
                    
                    <div class="movie-details-grid">
                        <div class="detail-item">
                            <strong>Producer:</strong> ${movie.producerName || 'Unknown'}
                        </div>
                        <div class="detail-item">
                            <strong>Director:</strong> ${movie.directorName || 'TBA'}
                        </div>
                        <div class="detail-item">
                            <strong>Budget:</strong> $${formatNumber(movie.budget)}
                        </div>
                        <div class="detail-item">
                            <strong>Raised:</strong> $${formatNumber(movie.raisedAmount || 0)}
                        </div>
                        ${movie.expectedReturnPercentage ? 
                            `<div class="detail-item">
                                <strong>Expected Return:</strong> ${movie.expectedReturnPercentage}%
                            </div>` : ''}
                        ${movie.releaseDate ? 
                            `<div class="detail-item">
                                <strong>Release Date:</strong> ${new Date(movie.releaseDate).toLocaleDateString()}
                            </div>` : ''}
                    </div>

                    <div class="funding-section">
                        <div class="funding-progress-large">
                            <div class="progress-bar-large">
                                <div class="progress-fill" style="width: ${fundingProgress}%"></div>
                            </div>
                            <div class="progress-info">
                                <span>${fundingProgress}% funded</span>
                                <span>$${formatNumber(movie.raisedAmount || 0)} / $${formatNumber(movie.budget)}</span>
                            </div>
                        </div>
                        
                        ${movie.status === 'FUNDING' && apiService.getCurrentUser()?.role === 'INVESTOR' ? 
                            `<div class="investment-section">
                                <input type="number" id="investmentAmount" placeholder="Investment Amount ($)" min="1">
                                <button class="btn btn-primary" onclick="investInMovie(${movie.id})">Invest Now</button>
                            </div>` : ''}
                    </div>
                </div>
            </div>
        `;

        modal.style.display = 'flex';
    }

    // Function to handle movie investment
    window.investInMovie = async function(movieId) {
        const amount = document.getElementById('investmentAmount').value;
        const currentUser = apiService.getCurrentUser();
        
        if (!amount || amount <= 0) {
            alert('Please enter a valid investment amount.');
            return;
        }

        if (!currentUser) {
            alert('Please login to invest.');
            return;
        }

        try {
            const investmentData = {
                userId: currentUser.id,
                movieId: movieId,
                amount: parseFloat(amount)
            };

            const response = await apiService.createInvestment(investmentData);
            if (response.success) {
                alert('Investment successful! Transaction ID: ' + response.data.transactionId);
                // Refresh movie details and movies list
                loadMovies();
                document.getElementById('movieModal').style.display = 'none';
            }
        } catch (error) {
            console.error('Investment failed:', error);
            alert('Investment failed: ' + error.message);
        }
    };

    // Function to show loading state
    function showLoading(show) {
        if (moviesLoading) {
            moviesLoading.style.display = show ? 'flex' : 'none';
        }
    }

    // Function to show messages in movies grid
    function showMoviesMessage(message) {
        if (moviesGrid) {
            moviesGrid.innerHTML = `<div class="movies-message">${message}</div>`;
        }
    }

    // Function to format numbers
    function formatNumber(num) {
        return new Intl.NumberFormat().format(num);
    }

    // Filter functionality
    filterButtons.forEach(button => {
        button.addEventListener('click', () => {
            // Update active filter button
            filterButtons.forEach(btn => btn.classList.remove('active'));
            button.classList.add('active');
            
            currentFilter = button.dataset.filter;
            filterMovies(currentFilter);
        });
    });

    // Function to filter movies
    function filterMovies(filter) {
        let filteredMovies = allMovies;
        
        if (filter !== 'all') {
            filteredMovies = allMovies.filter(movie => 
                movie.status.toLowerCase() === filter.toLowerCase()
            );
        }
        
        displayMovies(filteredMovies);
    }

    // Close movie modal
    const closeMovie = document.getElementById('closeMovie');
    const movieModal = document.getElementById('movieModal');
    
    if (closeMovie) {
        closeMovie.addEventListener('click', () => {
            movieModal.style.display = 'none';
        });
    }

    // Close modal when clicking outside
    if (movieModal) {
        movieModal.addEventListener('click', (event) => {
            if (event.target === movieModal) {
                movieModal.style.display = 'none';
            }
        });
    }

    // Make loadMovies globally available
    window.loadMovies = loadMovies;
    
    // Initial load check - only load if user is authenticated
    if (apiService.isAuthenticated()) {
        loadMovies();
    } else {
        showMoviesMessage('Please login to browse movies.');
    }
});
