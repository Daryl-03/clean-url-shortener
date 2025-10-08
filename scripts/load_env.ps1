# Le chemin est relatif au script, donc on remonte d'un niveau pour trouver .env à la racine.
$envFile = ".\.env"

if (Test-Path $envFile) {
    Write-Host "Loading environment variables from $envFile..."

    # Lire le contenu du fichier
    foreach ($line in (Get-Content $envFile)) {
        # Ignorer les lignes vides et les commentaires
        if (-not [string]::IsNullOrWhiteSpace($line) -and -not $line.TrimStart().StartsWith("#")) {

            # Regex améliorée :
            # - Clés avec lettres, chiffres, underscores, points et tirets.
            # - Gère les valeurs avec ou sans guillemets simples/doubles.
            if ($line -match '^([\w.-]+)\s*=\s*(["'']?)(.*)\2$') {
                $key = $Matches[1]
                $value = $Matches[3]

                # Définir la variable d'environnement pour le processus PowerShell actuel
                [System.Environment]::SetEnvironmentVariable($key, $value, "Process")
                Write-Host "  -> Set $key"
            }
        }
    }
    Write-Host "Variables loaded."
} else {
    Write-Host "Warning: The file '$envFile' was not found. Make sure it exists in the project root."
}
