@props([
    'id',
    'title',
    'showCloseButton' => true
])

<div id="{{ $id }}" class="modal" tabindex="-1" style="display: none;">
    <div class="modal-backdrop" onclick="closeModal('{{ $id }}')"></div>
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">{{ $title }}</h5>
                @if($showCloseButton)
                    <button type="button" class="close-button" onclick="closeModal('{{ $id }}')">&times;</button>
                @endif
            </div>
            <div class="modal-body">
                {{ $slot }}
            </div>
        </div>
    </div>
</div>

@once
    @push('styles')
    <style>
        .modal {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            z-index: 1050;
            overflow-x: hidden;
            overflow-y: auto;
            outline: 0;
        }
        .modal-backdrop {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.5);
            z-index: 1051;
        }
        .modal-dialog {
            position: relative;
            width: auto;
            margin: 1.75rem auto;
            max-width: 500px;
            z-index: 1052;
        }
        .modal-content {
            position: relative;
            display: flex;
            flex-direction: column;
            background: white;
            border-radius: 0.3rem;
            box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15);
        }
        .modal-header {
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 1rem;
            border-bottom: 1px solid #dee2e6;
        }
        .modal-body {
            position: relative;
            flex: 1 1 auto;
            padding: 1rem;
        }
        .modal-footer {
            display: flex;
            justify-content: flex-end;
            gap: 0.5rem;
            padding: 0.75rem;
            border-top: 1px solid #dee2e6;
        }
        .close-button {
            padding: 0.5rem;
            margin: -0.5rem -0.5rem -0.5rem auto;
            border: none;
            background: none;
            font-size: 1.5rem;
            cursor: pointer;
            opacity: 0.5;
            transition: opacity 0.15s;
        }
        .close-button:hover {
            opacity: 1;
        }
    </style>
    @endpush

    @push('scripts')
    <script>
        function showModal(id) {
            document.getElementById(id).style.display = 'block';
            document.body.style.overflow = 'hidden';
        }

        function closeModal(id) {
            document.getElementById(id).style.display = 'none';
            document.body.style.overflow = '';
        }
    </script>
    @endpush
@endonce