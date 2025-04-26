@extends('layouts.app')

@section('title', 'Edit Answer')

@section('additional-styles')
.edit-answer-container {
    background: white;
    padding: 2rem;
    border-radius: 8px;
    box-shadow: 0 1px 3px rgba(0,0,0,0.1);
    margin-bottom: 2rem;
}
.preview {
    margin-top: 1rem;
    padding: 1rem;
    border: 1px solid #e1e4e8;
    border-radius: 6px;
    background: #f8f9fa;
}
.code-preview {
    font-family: monospace;
    white-space: pre-wrap;
}
.form-label {
    font-weight: 500;
    margin-bottom: 0.5rem;
    color: #444;
}
.action-buttons {
    display: flex;
    gap: 1rem;
    margin-top: 1.5rem;
}
@endsection

@section('content')
<div class="container">
    <div class="mb-4">
        <a href="{{ route('questions.show', $answer->question) }}" class="text-decoration-none">
            <i class="bi bi-arrow-left"></i> Back to Question
        </a>
    </div>

    <div class="edit-answer-container">
        <h1 class="mb-4">Edit Your Answer</h1>

        @if($errors->any())
            <div class="alert alert-danger">
                <ul class="mb-0">
                    @foreach($errors->all() as $error)
                        <li>{{ $error }}</li>
                    @endforeach
                </ul>
            </div>
        @endif

        <form method="POST" action="{{ route('answers.update', $answer) }}">
            @csrf
            @method('PUT')
            
            <div class="form-group mb-4">
                <label for="body" class="form-label">Answer</label>
                <textarea id="body" name="body" rows="10" required 
                    class="form-control @error('body') is-invalid @enderror"
                    placeholder="Share your knowledge and help others learn">{{ old('body', $answer->body) }}</textarea>
                @error('body')
                    <div class="invalid-feedback">{{ $message }}</div>
                @enderror
            </div>

            <div class="form-group mb-4">
                <label for="code_snippet" class="form-label">Code (optional)</label>
                <textarea id="code_snippet" name="code_snippet" rows="8"
                    class="form-control font-monospace @error('code_snippet') is-invalid @enderror"
                    placeholder="Share your code if it helps explain your answer">{{ old('code_snippet', $answer->code_snippet) }}</textarea>
                <div id="code-preview" class="preview mt-3" style="display: none;">
                    <pre><code class="language-php"></code></pre>
                </div>
                @error('code_snippet')
                    <div class="invalid-feedback">{{ $message }}</div>
                @enderror
            </div>

            <div class="action-buttons">
                <button type="submit" class="btn btn-primary">
                    <i class="bi bi-check-lg"></i> Save Changes
                </button>
                <a href="{{ route('questions.show', $answer->question) }}" class="btn btn-outline-secondary">
                    <i class="bi bi-x"></i> Cancel
                </a>
            </div>
        </form>
    </div>
</div>
@endsection

@section('scripts')
<script>
document.addEventListener('DOMContentLoaded', function() {
    const codeInput = document.getElementById('code_snippet');
    const preview = document.getElementById('code-preview');
    const code = preview.querySelector('code');

    function updatePreview() {
        if (codeInput.value.trim()) {
            code.textContent = codeInput.value;
            preview.style.display = 'block';
            Prism.highlightElement(code);
        } else {
            preview.style.display = 'none';
        }
    }

    // Initialize preview
    updatePreview();

    // Update preview on input
    codeInput.addEventListener('input', updatePreview);
});
</script>
@endsection